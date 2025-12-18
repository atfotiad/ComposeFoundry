package com.atfotiad.composefoundry.ksp

import com.atfotiad.composefoundry.annotations.Destination
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSFunctionDeclaration
import com.google.devtools.ksp.validate
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.MemberName
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.writeTo

class NavigationProcessor(
    private val codeGenerator: CodeGenerator,
    private val logger: KSPLogger
) : SymbolProcessor {

    override fun process(resolver: Resolver): List<KSAnnotated> {
        val symbols = resolver.getSymbolsWithAnnotation(Destination::class.qualifiedName!!)
            .filterIsInstance<KSFunctionDeclaration>()

        if (!symbols.iterator().hasNext()) return emptyList()

        // 1. Setup the file: Destinations.kt
        val packageName = "com.atfotiad.composefoundry.navigation"
        val fileSpec = FileSpec.builder(packageName, "Destinations")

        // 2. Setup the Sealed Class
        val sealedClassBuilder = TypeSpec.classBuilder("Destinations")
            .addModifiers(KModifier.SEALED)
            .addProperty(
                PropertySpec.builder("route", String::class)
                    .addModifiers(KModifier.ABSTRACT)
                    .build()
            )

        // 3. Setup the Extension Function: NavGraphBuilder.addGeneratedDestinations()
        val navBuilderFunction = FunSpec.builder("addGeneratedDestinations")
            .receiver(ClassName("androidx.navigation", "NavGraphBuilder"))

        // Imports needed for arguments
        val navArgumentFunc = MemberName("androidx.navigation", "navArgument")
        val navTypeClass = ClassName("androidx.navigation", "NavType")
        val composableFunc = MemberName("androidx.navigation.compose", "composable")

        // ---Track Top Level Destinations for BottomNav/Rail ---
        val topLevelDestinations = mutableListOf<String>()

        symbols.forEach { function ->
            val annotation =
                function.annotations.first { it.shortName.asString() == Destination::class.simpleName }
            val routeArg =
                annotation.arguments.find { it.name?.asString() == "route" }?.value as? String

            val isTopLevel =
                annotation.arguments.find { it.name?.asString() == "isTopLevel" }?.value as? Boolean
                    ?: false
            // Raw Route: e.g., "counter_detail/{count}" or "counter_screen"
            val rawRoute =
                if (routeArg.isNullOrEmpty()) function.simpleName.asString() else routeArg
            val objectName = function.simpleName.asString()

            if (isTopLevel) {
                topLevelDestinations.add(objectName)
            }

            // 4. PARSE PARAMETERS: Find strings inside { }
            val paramRegex = "\\{([^}]+)\\}".toRegex()
            val params = paramRegex.findAll(rawRoute).map { it.groupValues[1] }.toList()

            logger.warn("Processing: $objectName -> Route: $rawRoute, Params: $params")

            // --- A. Generate Destination Object ---
            val destinationObject = TypeSpec.objectBuilder(objectName)
                .superclass(ClassName(packageName, "Destinations"))
                .addProperty(
                    PropertySpec.builder("route", String::class)
                        .addModifiers(KModifier.OVERRIDE)
                        .initializer("%S", rawRoute)
                        .build()
                )

            // Generate createRoute() helper if params exist
            // Example: fun createRoute(count: String): String = "counter_detail/$count"
            if (params.isNotEmpty()) {
                val createRouteFun = FunSpec.builder("createRoute")
                    .returns(String::class)

                var returnStatement = rawRoute
                params.forEach { param ->
                    // For simplicity, we assume all path params are Strings for now.
                    // You can enhance this later to support Int/Boolean based on annotation config.
                    createRouteFun.addParameter(param, String::class)
                    returnStatement = returnStatement.replace("{$param}", "\$$param")
                }
                createRouteFun.addStatement("return %P", returnStatement)
                destinationObject.addFunction(createRouteFun.build())
            }

            sealedClassBuilder.addType(destinationObject.build())

            // --- B. Generate Composable Call ---
            // Construct: listOf(navArgument("count") { type = NavType.StringType }, ...)
            val argsListBlock = CodeBlock.builder()
            if (params.isEmpty()) {
                argsListBlock.add("emptyList()")
            } else {
                argsListBlock.add("listOf(\n")
                argsListBlock.indent()
                params.forEachIndexed { index, param ->
                    // Use %M for navArgument, %S for string literal param name
                    argsListBlock.add(
                        "%M(%S) { type = %T.StringType }",
                        navArgumentFunc,
                        param,
                        navTypeClass
                    )
                    if (index < params.size - 1) {
                        argsListBlock.add(",\n")
                    }
                }
                argsListBlock.unindent()
                argsListBlock.add("\n)")
            }

            // Generate: composable(Destinations.X.route, arguments = ...) { X() }
            val screenComposable =
                MemberName(function.packageName.asString(), function.simpleName.asString())
            val destinationsClass = ClassName(packageName, "Destinations")

            navBuilderFunction.addStatement(
                "%M(%T.%N.route, arguments = %L) { %M() }",
                composableFunc,
                destinationsClass,
                objectName,
                argsListBlock.build(),
                screenComposable
            )
        }

        // --- Generate Companion Object with topLevelItems list ---
        if (topLevelDestinations.isNotEmpty()) {
            val listType = ClassName("kotlin.collections", "List")
            val destinationType = ClassName(packageName, "Destinations")

            val listInitializer = CodeBlock.builder().add("listOf(")
            topLevelDestinations.forEachIndexed { index, name ->
                listInitializer.add("%N", name)
                if (index < topLevelDestinations.size - 1) listInitializer.add(", ")
            }
            listInitializer.add(")")

            val companionObject = TypeSpec.companionObjectBuilder()
                .addProperty(
                    PropertySpec.builder("topLevelItems", listType.parameterizedBy(destinationType))
                        .initializer(listInitializer.build())
                        .build()
                )
                .build()

            sealedClassBuilder.addType(companionObject)
        }

        fileSpec.addType(sealedClassBuilder.build())
        fileSpec.addFunction(navBuilderFunction.build())

        fileSpec.build().writeTo(
            codeGenerator,
            Dependencies(true, *symbols.mapNotNull { it.containingFile }.toList().toTypedArray())
        )

        return symbols.filterNot { it.validate() }.toList()
    }
}

package org.runestar.client.patch

import org.apache.maven.model.Resource
import org.apache.maven.plugin.AbstractMojo
import org.apache.maven.plugins.annotations.LifecyclePhase
import org.apache.maven.plugins.annotations.Mojo
import org.apache.maven.plugins.annotations.Parameter
import org.apache.maven.project.MavenProject
import org.runestar.client.common.xorAssign
import org.runestar.client.updater.cleanGamepackFile
import org.runestar.client.updater.gamepackFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

@Mojo(
        name = "patch",
        defaultPhase = LifecyclePhase.GENERATE_RESOURCES
)
class PatchMojo : AbstractMojo() {

    @Parameter(defaultValue = "\${project}")
    private lateinit var project: MavenProject

    override fun execute() {
        val dir = Paths.get(project.build.directory)

        val cleanJar = dir.resolve("gamepack.clean.jar")
        cleanGamepackFile.openStream().use { input ->
            Files.copy(input, cleanJar, StandardCopyOption.REPLACE_EXISTING)
        }

        val injectedJar = dir.resolve("gamepack.inject.jar")
        inject(cleanJar, injectedJar)

        val generatedResourcesDir = dir.resolve("generated-resources")
        Files.createDirectories(generatedResourcesDir)
        val diffFile = generatedResourcesDir.resolve("gamepack.diff")
        val bytes = Files.readAllBytes(injectedJar)
        val original = gamepackFile.readBytes()
        bytes.xorAssign(original)
        Files.write(diffFile, bytes)
        project.addResource(Resource().apply {
            directory = generatedResourcesDir.toString()
        })
    }
}
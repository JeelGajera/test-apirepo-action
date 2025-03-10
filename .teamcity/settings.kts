import jetbrains.buildServer.configs.kotlin.*
import jetbrains.buildServer.configs.kotlin.buildFeatures.perfmon
import jetbrains.buildServer.configs.kotlin.buildSteps.nodeJS
import jetbrains.buildServer.configs.kotlin.triggers.vcs
import jetbrains.buildServer.configs.kotlin.vcs.GitVcsRoot

/*
The settings script is an entry point for defining a TeamCity
project hierarchy. The script should contain a single call to the
project() function with a Project instance or an init function as
an argument.

VcsRoots, BuildTypes, Templates, and subprojects can be
registered inside the project using the vcsRoot(), buildType(),
template(), and subProject() methods respectively.

To debug settings scripts in command-line, run the

    mvnDebug org.jetbrains.teamcity:teamcity-configs-maven-plugin:generate

command and attach your debugger to the port 8000.

To debug in IntelliJ Idea, open the 'Maven Projects' tool window (View
-> Tool Windows -> Maven Projects), find the generate task node
(Plugins -> teamcity-configs -> teamcity-configs:generate), the
'Debug' option is available in the context menu for the task.
*/

version = "2024.12"

project {

    vcsRoot(HttpsGithubComJeelGajeraTestApirepoActionRefsHeadsMain)

    buildType(Build)
}

object Build : BuildType({
    name = "Build"

    vcs {
        root(HttpsGithubComJeelGajeraTestApirepoActionRefsHeadsMain)
    }

    steps {
        nodeJS {
            name = "Build Project & Install Deps"
            id = "Build_Project_Install_Deps"
            shellScript = """
                npm ci
                npm run build
            """.trimIndent()
        }
        step {
            name = "Vulnerability Scan"
            id = "Vulnerability_Scan"
            type = "vulnerabilityScanRunner"
            param("deploymentUrl", "https://demo.com")
        }
    }

    triggers {
        vcs {
        }
    }

    features {
        perfmon {
        }
    }
})

object HttpsGithubComJeelGajeraTestApirepoActionRefsHeadsMain : GitVcsRoot({
    name = "https://github.com/JeelGajera/test-apirepo-action#refs/heads/main"
    url = "https://github.com/JeelGajera/test-apirepo-action"
    branch = "refs/heads/main"
    branchSpec = "refs/heads/*"
})

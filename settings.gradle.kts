pluginManagement {
    repositories {
        google {  // Limite le scope aux groupes spécifiques pour éviter des conflits
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()  // Dépendances provenant de Maven Central
        gradlePluginPortal()  // Dépôt pour les plugins Gradle
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)  // Interdit les repositories dans les projets individuels
    repositories {
        google()  // Nécessaire pour Firebase et les dépendances Android
        mavenCentral()  // Maven Central pour les autres dépendances
    }
}

rootProject.name = "GestionAbscenceEnseignants"
include(":app")

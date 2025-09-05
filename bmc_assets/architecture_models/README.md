# BMC Architecture Design Models

The origins of these models are traditional books and strategies you will find cited in the reference on each of them.

## What

### Each subfolder under `architecture_models` holds an architecture design model / strategy by name.

Each folder has:

- A Markdown file describing the architecture
- A text file `<architecture_name>_structure.txt` with the folder structure as a map of:
    - **key**: folder path
    - **value**: markdown documentation (can be empty)
- A `readme_templates` folder containing:
    - Short Description / Documentation for each folder
    - Short "main" Description / Documentation of the architecture

### At the top level there are two relevant files:

- [architecture_model_generator.sh](architecture_model_generator.sh):
    - A Bash script in charge of generating the whole architecture folder structure for a given architecture based on the
      `<architecture_name>_structure.txt` file and the `readme_templates`.
- [model_config.env](model_config.env):
    - A configuration file to select options like the app name, model to create, root directory, etc.

###

## Why

I could not find folder structures that would fit what I wanted to achieve regarding a few architectural "techniques" so I set to create my own, based
on tried patterns.

Once I hit a relatively stable structure, I added small documentation snippets to make it easy to understand the purpose of each folder.

When all that was stable enough, I added a bash script to be able to quickly start a project with a chosen architectural to automate the process.

The bash script and the way to configure the architecture styles are made so anyone can benefit from it regardless of the implementation language of
choice.
<br> For those using most JVM-based ones, there is [a project that creates a jar](https://github.com/bmc-anvil/bmc.dev.resources.code.bmc-arch-models)
based on this repository.

All this initial effort is focused on automation, consistency and maintainability.

These architecture design models are created almost at the very beginning of the whole project, they will therefore change as I encounter better /
different ways to approach a given design.

## How

With the generator script:

- Clone the repository.
- Configure the env file to your liking.
- Run the script.

With Maven:

- maven-plugin:
    - The resources' repository generates a maven plugin that allows incorporating all resources, including the architecture templates to any project.
    - Follow the instructions on the [repo's README.md](../../README.md) for installation, configuration and usage.

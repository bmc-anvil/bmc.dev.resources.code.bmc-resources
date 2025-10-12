# BMC Architecture Design Models

The origins of these models are traditional books and strategies you may find cited in the reference section on each of them.

### Currently supported models:

- `clean/DDD/hexa`:
    - A blend between the three approaches that work ok for me for the moment.
    - [click for description, design approach, folder tree view, etc.](/bmc_assets/architecture_models/clean_ddd_hexa/clean_ddd_hexa.md)

## What

### Each subfolder under `architecture_models` holds an architecture design model / strategy by name.

Each folder has:

- A Markdown file describing the architecture
- A text file `<architecture_name>.config` with the folder structure as a map of:
    - **key**: folder path
    - **value**: markdown documentation (can be empty)
- A `readme_templates` folder containing:
    - Short Description / Documentation for each folder
    - Short "main" Description / Documentation of the architecture

The config file is nothing other than a sort of csv, so it can expand in the future.

### At the top level there are two relevant files:

- [architecture_model_generator.sh](architecture_model_generator.sh):
    - A Bash script in charge of generating the whole architecture folder structure for a given architecture based on the
      `<architecture_name>.config` file and the `readme_templates`.
- [model_config.env](model_config.env):
    - A configuration file to select options like the app name, model to create, root directory, etc.

I included this script as not everybody uses or likes the JVM, so everyone can use and play with these structures and modify them to their liking.

## Why

I could not find folder structures that would fit what I wanted to achieve regarding a few architectural "techniques" so I set to create my own, based
on tried patterns.

Once I hit a relatively stable structure, I added small documentation snippets to make it easy to understand the purpose of each folder.

When all that was stable enough, I added a bash script to be able to quickly start a project with a chosen architectural to automate the process.

The bash script and the way to configure the architecture styles are made so anyone can benefit from it regardless of the implementation language of
choice.
<br> For those using maven, there is a maven plugin built by this repository that handles the creation of an architecture design.

All this initial effort is focused on automation, consistency and maintainability.

These architecture design models are created almost at the very beginning of the whole project, they will therefore change as I encounter better /
different ways to approach a given design.

## How

#### With the generator script:

- Clone the repository (maybe just the [architecture_models](/bmc_assets/architecture_models) folder...).
- Configure the env file to your liking.
- Run the script.

#### With Maven:

- The resources' repository generates a maven plugin that allows incorporating all resources, including the architecture templates to any project.
- Follow the instructions on the [plugin's README.md](/README_MAVEN_PLUGIN.md) for installation, configuration and usage.

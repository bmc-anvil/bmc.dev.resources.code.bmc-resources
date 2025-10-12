# BMC Resources and Architecture Design Maven Plugin

This Document is a user manual for the **bmc-resources** maven plugin.

There will be a dedicated technical page on the whole BMC Anvil project site.

## What

This plugin generates two things:

- A given architecture folder structure that has been defined in its [corresponding model folder](/bmc_assets/architecture_models).
- A set of upstream and user resources that have been defined in their [corresponding configuration files](/bmc_assets/resources_config).

## Why

The reasons leading to this plugin were:

- To be able to automate the generation of architecture structures
- To be able to share common resources
- To do both things "automatically" or more specifically, programmatically generate those assets.

The reason for a plugin instead of other means fo sharing and creating files and folder resides in the distribution and minimal user intervention a plugin
provides.

Because the plugin can be incorporated to parent poms and executed on the children's builds automatically, the automation eventually requires zero user
intervention bar a few configurations.

The above allows producing repeatable projects with full consistency across the board, while also allowing configuring what is specific for a given project.

Given how core it is, the plugin is also configurable to be skipped altogether, or in part.

### The anecdote behind this (and others) decision:

Not every project has a given set of rules to unburden developers to configure over and over again linters, ignores, styles, etc.
At the same time, there is no set way to do hexagonal arch or clean or ddd or layered or... or... The books are specific on some rules and very broad on
how to implement them consistently.

So it happened at a company that each team was in charge of a few related services.
<br>Each team implemented the same strategies in their own way, had their own rules for coding, styling, linting, failing builds etc.

Everything was unique(ish) for each team.

The result was, and to mention just a very innocent few:

- Changing teams had a penalty almost as switching companies.
- Devs were usually consulted by their old teams to explain "what why how" on things that seem to not make sense nor fit anywhere.
- Inconsistencies where the norm to the point even within the same team, same repo, same file... you could see each dev was playing dice with how to write
  things.
- On call troubleshooting devs wasted time trying to understand things outside their maintained repos.
- The understanding and implementation of architectural styles was per team, so we could encounter that:
    - DDD's factories implemented in a few teams did not exist on other teams favoring the GOF factory pattern where needed.
    - Use-cases we grouped by similar concerns on a team and were unique on others and grouped in folders and were called services on others.
    - Projects created by the same developer on the same team used different naming for the same architecture strategies
    - Naming conventions were non-existing, so in the end the same things on different projects ended up somewhere else.
- Creating projects would require one of the "experienced" members as no one knew what was where, or which project to use as a template.
- [...]

Bottom line:
> Maintenance and reusability was every nightmare described in clean arch books.
> The amount of time adding features would increase the bigger each service or project grew up, until basically services were broken down. Given they were
> broken down and started with the same processes, at first things ran ok, and little by little the same obstacles started to pile again.

This plugin, along with the rest of the consistency and repeatability guards across all BMC, aims to reduce all those problems as much as possible.

The plugin along with the architecture model and the resources reflect what I know / need now.
<br>All will evolve as requirements and discoveries do.

## How: The User Guide.

### Goals OverView

The plugin has one goal:

- bmc-resources:generate is the goal that generates the chosen architecture model and copies BMC base resources to local.

### Usage:

The plugin is added to the build section of a Maven's pom.xml file.

Check the snippet below for an example with all the plugin's properties.

```xml

<project>
    ...
    <build>
        <plugins>
            ...
            <plugin>
                <groupId>bmc.dev.resources.code</groupId>
                <artifactId>bmc-resources</artifactId>
                <version>bmc-local</version>
                <executions>
                    <execution>
                        <phase>generate-sources</phase>                       <!-- 0 -->
                        <goals>
                            <goal>generate</goal>                             <!-- 1 -->
                        </goals>
                    </execution>
                </executions>
                <configuration>
                    <generalConfig>                                           <!-- 2 -->
                        <skip>true</skip>                                     <!-- 2.1 -->
                    </generalConfig>
                    <architecture>                                            <!-- 3 -->
                        <mainReadme/>                                         <!-- 3.1 -->
                        <model>clean_ddd_hexa</model>                         <!-- 3.2 -->
                        <skip>false</skip>                                    <!-- 3.3 -->
                        <skipReadme>false</skipReadme>                        <!-- 3.4 -->
                    </architecture>
                    <resources>                                               <!-- 4 -->
                        <skip>false</skip>                                    <!-- 4.1 -->
                        <overwriteUserResources>true</overwriteUserResources> <!-- 4.2 -->
                    </resources>
                </configuration>
            </plugin>
            ...
        </plugins>
    </build>
    ...
</project>
```

#### 0: Execution

The execution phase is by default the `generate-sources` one.

#### 1: Goal

There is just one goal in this plugin which is `generate`.

It is in charge of copying all resources to their configured folders and to generate the chosen architecture model.

Once the goal runs successfully, the architecture and user resources jobs are no longer executed and only the upstream resources are run again.

#### 2: General Configuration

These configuration entries affect the entire plugin behavior.

Momentarily there is only one option, but if something affects the entire plugin execution, this section is where it will appear.

##### 2.1: `skip`

Skips the execution of the plugin altogether.

- Type: Boolean.
- Default: false
- Required: false.
- Behavior: by default, the plugin runs.

#### 3: Architecture Configuration

These configuration entries control the architecture model creation.

##### 3.1: `mainReadMe` (no use for now)

In case there is a specific global README.md file to place at the root of the folder structure, you can configure it here.

This will look for a given readme file at the model's root folder in the plugin's jar.

Momentarily this has no use as everything is packaged in a jar, therefore, options are limited. It is a placeholder for future implementations.

Values:

- Type: String (sase sensitive)
- Options:
    - Name of the mainReadme file:
    - ie. core.md, Core.md
- Default: null.
- Required: false.
- Behavior: by default, the main readme is the one that has the same name as the architecture's model.

##### 3.2: `model`

This is the model's name to use to create a given architecture folder structure.

It has to be set to create an architecture structure, there is no default design architecture selected.

Values:

- Type: String (sase sensitive)
- Options:
    - Models currently supported:
        - `clean_ddd_hexa`
- Default: none.
- Required: true.
- Behavior: by default, there is no model set.
    - Once one is set, the plugin will try to look for it in its own jar and create the configured structure.
    - If none is set, the architecture creation is skip.
    - If the one set cannot be found, an error is displayed and the plugin stops to allow to correct.

##### 3.3: `skip`

Skips the execution of the architecture generation.

Values:

- Type: Boolean.
- Default: false
- Required: false
- Behavior: by default, the architecture's model generation runs.

##### 3.4: `skipReadme`

Skips the execution of the architecture's accompanying readme files generation. The folder structure is still generated.

Values:

- Type: Boolean.
- Default: false
- Required: false
- Behavior: by default, the accompanying readme files are created along the architecture folder structure.

#### 4: Resources

##### 4.1: `skip`

Skips the execution of the resources' copy.

Values:

- Type: Boolean.
- Default: false
- Required: false
- Behavior: by default, the resources are copied from the jar to their local destination.

##### 4.2: `overwriteUserResources`

Overwrites user resources.

Values:

- Type: Boolean.
- Default: false
- Required: false
- Behavior: by default, user resources are written once and left alone.
    - Setting this to true will overwrite the user resources.

> Note: Although this is a destructive operation with no undo, in a standard setup vcs or the ide will track the history of changes.
>
> This vcs/ide possibility is an assumption that you should not consider at all if you do not track changes or made several changes to your user resources.


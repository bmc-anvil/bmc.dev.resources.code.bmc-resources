# Resources

Except for the resources that this project uses itself and that for a reason need to be at the top level folder (.editor.config, .gitignore, etc.), the rest
are all under the [bmc_assets](bmc_assets) folder.

I chose to have all assets outside the traditional resources folder for a few reasons:

- I already have to have a few files outside resources, like the top level ones.
- Inside the assets folder I have not only "pure resources" but also configuration, files, shell scripts, documentation for each architecture model, etc. Those
  files make no sense to reside inside the traditional resources' folder.

This approach separates java code for the maven plugin and its resources, from the general broader resources and models.

### To be included and packaged:

#### Upstream resources:

These resources always override the ones already present.

Linters, stylers, some default scripts, etc. are among these resources.

| Name            | type   | Description                                |
|-----------------|--------|--------------------------------------------|
| .mvn            | folder | maven wrapper downloader and config files  |
| --------------- | ------ | ------------------------------------------ |
| .gitignore      | file   | git-ignore rules                           |
| .editorconfig   | file   | formatting rules with IntelliJ IDEA rules  |
| checkstyle.xml  | file   | checkstyle file                            |
| mvnw            | file   | Maven wrapper executable script file       |

#### User resources:

These resources are copied once and are not overridden unless configured to do so.
These can include GitHub workflows, maven or jvm configuration, licenses, etc.

| Name            | type   | Description                                |
|-----------------|--------|--------------------------------------------|
| .github         | folder | basic GitHub actions workflow              |
| --------------- | ------ | ------------------------------------------ |
| .dockerignore   | file   | docker-ignore rules                        |
| LICENSE         | file   | Common license file                        |

### Not to be included:

Anything not included in the above list is of no interest or are files / folders kept for historical / configuration or bookkeeping reasons, i.e.,
[bare_metal_code_style_intellij.xml](bmc_assets/bare_metal_code_style_intellij.xml)

`bare_metal_code_style_intellij.xml` are my intellij code-style settings, kept here for easy recovery, sharing if anyone is interested and for some additional
configuration that `.editorConfig` does not support.

## Configuration files:

Inside the [resources_config](bmc_assets/resources_config) folder there are two files:

- [bmc_resources_upstream.config](bmc_assets/resources_config/bmc_resources_upstream.config)
- [bmc_resources_user.config](bmc_assets/resources_config/bmc_resources_user.config)

The files describe which sources will always be overwritten (upstream) and which ones will be created once and then left for the user to modify(user).

The structure of the config file is a csv as follows

- no header
- source location from jar's root, target location on disk from the project's root, if it is executable (marked with the letter `x`)
- if an input ends with `/` it means it is a directory.
    - _yes, it is possible to detect if a given path is a file or a directory, but it won't be clear by reading a configuration file._

**Example 1:**

```text
mvnw,mvnw,x
```

This means the mvnw file at the root of the jar file will be copied at the root of the project and will be changed to be executable.

**Example 2:**

```text
./mvn/,./mvnw/
```

This means the .mvn directory and everything under it, even subdirectories, at the root of the jar file will be copied at the root of the project.

**Example 3:**

```text
./mvn/,./files/maven/
```

This means the .mvn directory and everything under it, even subdirectories, at the root of the jar file will be copied at the root of the project under the
.files/maven directory.




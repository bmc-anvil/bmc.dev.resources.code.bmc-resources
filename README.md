# bmc-codestyle

The tricky part is the cyclic dependency of this project to keep consistency on the parent
exclude checkstyle from this pom maybe. or else:

first install this project to generate a jar, then add the jar to the bmc-bom project as plugin of checkstyle
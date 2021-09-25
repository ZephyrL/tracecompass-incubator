Eclipse Trace Compass Incubator
===============================

This source tree contains the source code for Trace Compass Incubator plugins.

Project rules and guidelines
----------------------------

This project is an incubator which aims at rapid availability of new features and prototypes, to provide new functionnalities to end users and get feedback from them during the development process. As such, code style and design architecture will not be looked at too much during reviews, though some advices and ideas can be discussed. The features have to work as expected though and not break anything in Trace Compass.

* Add unit tests: CI will be run on every patch on gerrit, so having unit tests ensures that a patch does not break anything somewhere else. Also, these plugins are work in progress. Having unit tests makes it easier to manipulate code structure and algorithms by knowing the expected results. Tests need to be maintained, but have more benefits than trouble.

* There is no release, so no API. Plugins may use the other incubation features at their own risk.

* The incubator is an Eclipse project so code will be verified to make sure it can be distributed by Eclipse. Committers are responsible to ensure the IP due diligence is met with every patch submitted.

When the code base of some feature gets more stable and mature, it may be ported to the main Trace Compass repository and only then will there be thorough code review and design discussion.

Setting up the development environment
--------------------------------------

To set up the environment to build Trace Compass from within Eclipse, see this
wiki page:
<http://wiki.eclipse.org/Trace_Compass/Development_Environment_Setup>

Once the Trace Compass environment set, import the projects from this repository.

Compiling manually
------------------

The Maven project build requires version 3.3 or later. It can be downloaded from
<http://maven.apache.org> or from the package management system of your distro.

To build the project manually using Maven, simply run the following command from
the top-level directory:

    mvn clean install

The default command will compile and run the unit tests. Running the tests can
take some time, to skip them you can append `-Dmaven.test.skip=true` to the
`mvn` command:

    mvn clean install -Dmaven.test.skip=true

The resulting executables will be in the
`rcp/org.eclipse.tracecompass.incubator.rcp.product/target/products`. There are
the archives for linux, macos and Windows. The sub-directories
`org.eclipse.tracecompass.incubator.rcp/<os>/...` contain the executable for each
OS.

To generate the javadoc from the Trace Compass source code, run the following
command from the top-level directory:

    mvn clean package javadoc:aggregate

The javadoc html files will be under `target/site/apidocs`.

Maven profiles and properties
-----------------------------

The following Maven profiles and properties are defined in
the build system. You can set them by using `-P[profile name]` and
`-D[property name]=[value]` in `mvn` commands.

* `-Pdeploy-update-site`

  Mainly for use on build servers. Copies the standard update site (for the
  Eclipse plugin installation) to the destination specified by
 `-DsiteDestination=/absolute/path/to/destination`.

* `mvn javadoc:aggregate`

  Mainly for use on build servers. Generates the javadoc of API classes as a
  HTML website to the destination specified by `-Djavadoc-site=/absolute/path/to/destination`.
## Java Pathfinder Analytic Module configuration guidelines
This repository includes modules for JPF trace integration that support importing, analyzing and visualizing JPF trace logs. The JPF trace modules are added as a trace type extension, as Trace Compass has already enables a number of common trace types (CTF, Android Atrace, JsonTrace, etc.), 

### 0. Helpful links and documents

[The Trace Compass Incubator project entrance](https://projects.eclipse.org/projects/tools.tracecompass.incubator/developer)

[**The developer guide for creating new trace types for TC***](https://archive.eclipse.org/tracecompass/doc/org.eclipse.tracecompass.doc.dev/Implementing-a-New-Trace-Type.html#Implementing_a_New_Trace_Type) 

\* Before you start, please make sure you have taken a look of this chapter, it's not necessary to get full hang of every step, but it's helpful to know how are JPF trace modules organized.

### 1. Location of JPF modules

The main functional sections lies under the following named folders:

* tracetypes/org.eclipse.tracecompass.incubator.jpftrace

* tracetypes/org.eclipse.tracecompass.incubator.jpftrace.core
* tracetypes/org.eclipse.tracecompass.incubator.jpftrace.ui

The *core* module undertakes the jobs to import, resolve and store the JPF trace timeline into the backend, while the *ui* module creates flow charts for the trace. You may find other submodules named after **jpftrace*, which are atomatically generated by Trace Compass when we were starting to introduce new trace type on Trace Compass. These modules are not implemented, and is not necessory through build. These modules are possibly generated at the following  locations:

* tracetypes/org.eclipse.tracecompass.incubator.jpftrace.core.tests
* tracetypes/org.eclipse.tracecompass.incubator.jpftrace.ui.swtbot.tests
* doc/org.eclipse.tracecompass.incubator.jpftrace.doc.user



### 2. The POM build tree

When build with Maven, the tool firstly traverses through the POM tree of the project in order and registers modules to build. Each folder at any level of the project has it own POM.xml file, which lists all the folders the build tool should inspect in the next iteration. For the build of JPF trace modules, check **tracetypes/pom.xml**, and **doc/pom.xml** there have listed several JPF modules mentioned in the above section, you may find some unneeded modules are also in the list, don't worry and feel free to remove them, the project should build no matter those modules are included.   



### 3. Misc changes besides the newly added modules, don't miss them!

The module-creation function of Trace Compass does not ony place the module folders, but should have also modified a few files regarding the build process, (otherwise the new trace type would exist in the built product). If you are redoing the steps from a clean incubator project, you have no worries, but if you are migrating the code by copying and pasting, **be careful**, you have to check these files and configure them manually:

* common/org.eclipse.tracecompass.incubator.releng-site/category.xml 

  add:

  ``` xml
  <feature url="features/org.eclipse.tracecompass.incubator.jpftrace_0.0.0.qualifier.jar" id="org.eclipse.tracecompass.incubator.jpftrace" version="0.0.0">
        <category name="trace-types"/>
  </feature>
  ```

* rcp/org.eclipse.tracecompass.incubator.rcp.product/tracing.incubator.product

  add:

  ```xml
  <features>
     ...
     <feature id="org.eclipse.tracecompass.incubator.jpftrace"/>    // Add this line ONLY!
  </features>
  ```

### 4. Modular dependency: Manifest files

Each module folder also contains a MANIFEST.MF below META-INF, it contains modular dependencies of the current module, especially when you are implementing interfaces or need to rely on any common features. You may have to manually add the dependency here, check **tracetypes/org.eclipse.tracecompass.incubator.jpftrace.core/META-INF/MANIFEST.MF **as an example.

### 5. For faster build 

Your first build of the project with Maven need to fetch a greate number of dependencies, if any of the download fails due to unstable connection, the build will possilby fail, so here is an emperical suggestion: disable unnecessary modules in the root POM file, like the following:

```xml
In {root dir of TC incubator}/pom.xml

<modules>
    <module>common</module>
    <module>analyses</module>
    <module>callstack</module>
    <!-- <module>vm</module> -->
    <!-- <module>scripting</module> -->
    <module>tracetypes</module>
    <!-- <module>trace-server</module> -->
    <!-- <module>doc</module> -->
    <module>rcp</module>
  </modules>
```
package io.codearte.accurest.maven;

import java.io.File;
import java.util.regex.Pattern;

import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import io.takari.maven.testing.TestProperties;
import io.takari.maven.testing.TestResources;
import io.takari.maven.testing.executor.MavenRuntime;
import io.takari.maven.testing.executor.MavenVersions;
import io.takari.maven.testing.executor.junit.MavenJUnitTestRunner;

@RunWith(MavenJUnitTestRunner.class)
@MavenVersions({ "3.3.3" })
public class PluginIT {

	@Rule
	public final TestResources resources = new TestResources();

	private final TestProperties properties = new TestProperties();

	private final MavenRuntime maven;

	public PluginIT(MavenRuntime.MavenRuntimeBuilder mavenBuilder) throws Exception {
		this.maven = mavenBuilder.withCliOptions("-B", "-U").build();
	}

	@Test
	public void should_build_project_Spring_Boot_Groovy_with_Accurest() throws Exception {
		File basedir = resources.getBasedir("spring-boot-groovy");
		maven.forProject(basedir)
				.execute("package")
				.assertErrorFreeLog()
				.assertLogText("Generating server tests source code for Accurest contract verification")
				.assertLogText("Generated 1 test classes.")
				.assertLogText("Converting from accurest contracts written in GroovyDSL to WireMock stubs mappings")
				.assertLogText("Creating new json")
				.assertLogText("Running io.codearte.accurest.tests.AccurestSpec")
				.assertErrorFreeLog();
	}

	@Test
	public void should_build_project_Spring_Boot_Java_with_Accurest() throws Exception {
		File basedir = resources.getBasedir("spring-boot-java");
		maven.forProject(basedir)
				.execute("package")
				.assertErrorFreeLog()
				.assertLogText("Generating server tests source code for Accurest contract verification")
				.assertLogText("Generated 1 test classes.")
				.assertLogText("Converting from accurest contracts written in GroovyDSL to WireMock stubs mappings")
				.assertLogText("Creating new json")
				.assertLogText("Running io.codearte.accurest.tests.AccurestTest")
				.assertErrorFreeLog();
	}

	@Test
	public void should_build_project_with_plugin_extension() throws Exception {
		File basedir = resources.getBasedir("plugin-extension");
		maven.forProject(basedir)
				.execute("package")
				.assertErrorFreeLog()
				.assertLogText("Generating server tests source code for Accurest contract verification")
				.assertLogText("Generated 1 test classes.")
				.assertLogText("Converting from accurest contracts written in GroovyDSL to WireMock stubs mappings")
				.assertLogText("Creating new json")
				.assertLogText("Running io.codearte.accurest.tests.AccurestTest")
				.assertErrorFreeLog();
	}

	@Test
	@Ignore("Ignored, because of bug accurest#245")
	public void should_build_project_project_with_complex_configuration() throws Exception {
		File basedir = resources.getBasedir("complex-configuration");
		maven.forProject(basedir)
				.execute("package")
				.assertErrorFreeLog()
				.assertLogText("Tests run: 2, Failures: 0, Errors: 0, Skipped: 1")
				.assertLogText("Running com.blogspot.toomuchcoding.frauddetection.AccurestTest")
				.assertErrorFreeLog();
	}

	@Test
	public void should_convert_Accurest_Contracts_to_WireMock_Stubs_mappings() throws Exception {
		File basedir = resources.getBasedir("pomless");
		properties.getPluginVersion();
		maven.forProject(basedir)
				.withCliOption("-X")
				.execute(String.format("io.codearte.accurest:accurest-maven-plugin:%s:convert",
						properties.getPluginVersion()))
				.assertLogText("Converting from accurest contracts written in GroovyDSL to WireMock stubs mappings")
				.assertLogText("Creating new json")
				.assertErrorFreeLog();
	}
}
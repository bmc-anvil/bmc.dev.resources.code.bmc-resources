package bmc.dev.resources.code.support;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugin.logging.SystemStreamLog;

public class DummyMojoForTest extends AbstractMojo {

    private final Log log = new SystemStreamLog();

    @Override
    public void execute() {

    }

    @Override
    public Log getLog() {

        return log;
    }

}

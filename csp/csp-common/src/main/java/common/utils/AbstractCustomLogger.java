package common.utils;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

/**
 * �@ �~ �N �X �G<br>
 * �@ �~ �W �� �G<br>
 * �{ �� �N �� �GAbstractCustomLogger.java<br>
 * �y             �z �G<br>
 * ��             �q �GHongfang intelligent technology.<br><br>
 *�i �� �� �� ���j  �G<br>
 *�i �� �X �� ��j  �G<br>
 *�i �� �� �� ���j  �G<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2015/10/03<P>
 */
public abstract class AbstractCustomLogger extends MarkerIgnoringBase implements Logger {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8621787421983244344L;

    /**
     * ������ org.apache.commons.logging
     */
    private Log log = null;
    
    protected Class<?> loggerClazz;

    public AbstractCustomLogger(Class<?> clazz) {
    	super();
    	this.loggerClazz = clazz;
    	log = org.apache.commons.logging.LogFactory.getLog(clazz);
    }
    
    protected Log getLog() {
        return this.log;
    }
    
    /**
     * �blog�e�[�J�ϥΪ̸�T
     * 
     * @param log
     * @return
     */
    public abstract String customLogFormat(String log);
    
	@Override
	public void debug(String arg0) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug(this.customLogFormat(arg0));
        }
	}

	@Override
	public void debug(String arg0, Object arg1) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug(MessageFormatter.format(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void debug(String arg0, Object[] arg1) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug(MessageFormatter.arrayFormat(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void debug(String arg0, Throwable arg1) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug(this.customLogFormat(arg0), arg1);
        }
	}

	@Override
	public void debug(String arg0, Object arg1, Object arg2) {
        if (this.getLog().isDebugEnabled()) {
            this.getLog().debug(MessageFormatter.format(this.customLogFormat(arg0), arg1, arg2).getMessage());
        }
	}

	@Override
	public void error(String arg0) {
        if (this.getLog().isErrorEnabled()) {
            this.getLog().error(this.customLogFormat(arg0));
        }
	}

	@Override
	public void error(String arg0, Object arg1) {
        if (this.getLog().isErrorEnabled()) {
            this.getLog().error(MessageFormatter.format(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void error(String arg0, Object[] arg1) {
        if (this.getLog().isErrorEnabled()) {
            this.getLog().error(MessageFormatter.arrayFormat(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void error(String arg0, Throwable arg1) {
        if (this.getLog().isErrorEnabled()) {
            this.getLog().error(this.customLogFormat(arg0), arg1);
        }
	}

	@Override
	public void error(String arg0, Object arg1, Object arg2) {
        if (this.getLog().isErrorEnabled()) {
            this.getLog().error(MessageFormatter.format(this.customLogFormat(arg0), arg1, arg2).getMessage());
        }
	}

	@Override
	public void info(String arg0) {
        if (this.getLog().isInfoEnabled()) {
            this.getLog().info(this.customLogFormat(arg0));
        }	
	}

	@Override
	public void info(String arg0, Object arg1) {
        if (this.getLog().isInfoEnabled()) {
            this.getLog().info(MessageFormatter.format(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void info(String arg0, Object[] arg1) {
        if (this.getLog().isInfoEnabled()) {
            this.getLog().info(MessageFormatter.arrayFormat(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void info(String arg0, Throwable arg1) {
        if (this.getLog().isInfoEnabled()) {
            this.getLog().info(this.customLogFormat(arg0), arg1);
        }
	}

	@Override
	public void info(String arg0, Object arg1, Object arg2) {
        if (this.getLog().isInfoEnabled()) {
            this.getLog().info(MessageFormatter.format(this.customLogFormat(arg0), arg1, arg2).getMessage());
        }
	}

	@Override
	public boolean isDebugEnabled() {
		return this.getLog().isDebugEnabled();
	}

	@Override
	public boolean isErrorEnabled() {
		return this.getLog().isErrorEnabled();
	}

	@Override
	public boolean isInfoEnabled() {
		return this.getLog().isInfoEnabled();
	}

	@Override
	public boolean isTraceEnabled() {
		return this.getLog().isTraceEnabled();

	}

	@Override
	public boolean isWarnEnabled() {
		return this.getLog().isWarnEnabled();
	}

	@Override
	public void trace(String arg0) {
        if (this.getLog().isTraceEnabled()) {
            this.getLog().trace(this.customLogFormat(arg0));
        }	
    }

	@Override
	public void trace(String arg0, Object arg1) {
        if (this.getLog().isTraceEnabled()) {
            this.getLog().trace(MessageFormatter.format(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void trace(String arg0, Object[] arg1) {
        if (this.getLog().isTraceEnabled()) {
            this.getLog().trace(MessageFormatter.arrayFormat(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void trace(String arg0, Throwable arg1) {
        if (this.getLog().isTraceEnabled()) {
            this.getLog().trace(this.customLogFormat(arg0), arg1);
        }
	}

	@Override
	public void trace(String arg0, Object arg1, Object arg2) {
        if (this.getLog().isTraceEnabled()) {
            this.getLog().trace(MessageFormatter.format(this.customLogFormat(arg0), arg1, arg2).getMessage());
        }
	}

	@Override
	public void warn(String arg0) {
        if (this.getLog().isWarnEnabled()) {
            this.getLog().warn(this.customLogFormat(arg0));
        }	
	}

	@Override
	public void warn(String arg0, Object arg1) {
        if (this.getLog().isWarnEnabled()) {
            this.getLog().warn(MessageFormatter.format(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void warn(String arg0, Object[] arg1) {
        if (this.getLog().isWarnEnabled()) {
            this.getLog().warn(MessageFormatter.arrayFormat(this.customLogFormat(arg0), arg1).getMessage());
        }
	}

	@Override
	public void warn(String arg0, Throwable arg1) {
        if (this.getLog().isWarnEnabled()) {
            this.getLog().warn(this.customLogFormat(arg0), arg1);
        }
	}

	@Override
	public void warn(String arg0, Object arg1, Object arg2) {
        if (this.getLog().isWarnEnabled()) {
            this.getLog().warn(MessageFormatter.format(this.customLogFormat(arg0), arg1, arg2).getMessage());
        }
	}

}

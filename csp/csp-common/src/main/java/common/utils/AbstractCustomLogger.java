package common.utils;
import org.apache.commons.logging.Log;
import org.slf4j.Logger;
import org.slf4j.helpers.MarkerIgnoringBase;
import org.slf4j.helpers.MessageFormatter;

/**
 * 作 業 代 碼 ：<br>
 * 作 業 名 稱 ：<br>
 * 程 式 代 號 ：AbstractCustomLogger.java<br>
 * 描             述 ：<br>
 * 公             司 ：Hongfang intelligent technology.<br><br>
 *【 資 料 來 源】  ：<br>
 *【 輸 出 報 表】  ：<br>
 *【 異 動 紀 錄】  ：<br>
 * @author   : Mark Wong <br>
 * @version  : 1.0.0 2015/10/03<P>
 */
public abstract class AbstractCustomLogger extends MarkerIgnoringBase implements Logger {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8621787421983244344L;

    /**
     * 先換成 org.apache.commons.logging
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
     * 在log前加入使用者資訊
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

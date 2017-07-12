package sb.common.plugin;

import java.util.Properties;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Intercepts({@Signature(
	type = Executor.class, 
	method = "query", 
	args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
)})
public class ExecutorPlugin implements Interceptor {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExecutorPlugin.class);

	private final int mappedStatementIndex = 0;
	private final int parameterIndex = 1;
	private final int rowboundsIndex = 2;
	//    private final int resultHandlerIndex = 3;    

	@Override
	public Object intercept(Invocation invocation) throws Throwable {
		MappedStatement mappedStatement = (MappedStatement)invocation.getArgs()[mappedStatementIndex];
		LOGGER.info("+> 매핑구문 아이디 : {}", mappedStatement.getId());

		Object parameter = invocation.getArgs()[parameterIndex];
		LOGGER.info("+> 파라미터 : {}", parameter);

		RowBounds rowBounds = (RowBounds)invocation.getArgs()[rowboundsIndex];
		LOGGER.info("+> RowBounds : limit - {}, offset - {}", rowBounds.getLimit(), rowBounds.getOffset());

		return invocation.proceed();
	}

	@Override
	public Object plugin(Object target) {
		return Plugin.wrap(target, this);
	}

	@Override
	public void setProperties(Properties properties) {
		LOGGER.info("{}", properties.getProperty("property"));
	}
}
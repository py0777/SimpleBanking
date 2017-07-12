package sb.common.type;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

public class DateTypeHandler implements TypeHandler<Date>  {
	@Override
	public Date getResult(ResultSet arg0, String arg1) throws SQLException {
		return null;
	}

	@Override
	public Date getResult(ResultSet arg0, int arg1) throws SQLException {
		return null;
	}

	@Override
	public Date getResult(CallableStatement arg0, int arg1) throws SQLException {
		return null;
	}

	@Override
	public void setParameter(PreparedStatement arg0, int arg1, Date arg2, JdbcType arg3) throws SQLException {
	}
}
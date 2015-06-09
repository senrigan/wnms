package com.gdc.nms.test.util.connection;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import javax.persistence.Column;




import com.gdc.nms.model.Device;
import com.gdc.nms.model.Device.CliProtocol;
import com.gdc.nms.model.Device.Type;
import com.gdc.nms.model.Project;
import com.gdc.nms.model.SnmpVersion;

import connection.credential.MysqlCredential;

import org.apache.commons.beanutils.BasicDynaBean;
import org.apache.commons.beanutils.BasicDynaClass;
import org.apache.commons.beanutils.DynaBean;
import org.apache.commons.beanutils.DynaProperty;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.beanutils.ResultSetDynaClass;

public class MySql {

    private static final CliProtocol[] CLIValues = CliProtocol.values();
    private static final Type[] TypeValues = Type.values();
    private static final SnmpVersion[] SnmpVersionValues = SnmpVersion.values();

    //private static final MySql INSTANCE = new MySql();

    private Connection connection;
    private String host;
    private String user;
    private String pass;
    private String dbname;
    private int port;
    private List<Device> devices;
    private List<Project> projects;

    public MySql() {
//        host = "127.0.0.1";
//        user = "root";
//        pass = "vitalnoc";
//        dbname = "nms";
//        port = 3306;
    }
    
    public MySql(String host,String user,String pass,String database,int port){
    	this.host=host;
    	this.user=user;
    	this.pass=pass;
    	this.dbname=database;
    }
    
    
    public MySql(MysqlCredential mysql){
    	this.host=mysql.getHostname();
    	this.user=mysql.getUsername();
    	this.pass=mysql.getPassword();
    	this.dbname=mysql.getDataBaseName();
    	this.port=mysql.getPort();
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getHost() {
        return host;
    }

    public void setUsername(String user) {
        this.user = user;
    }

    public String getUsername() {
        return user;
    }

    public void setPassword(String pass) {
        this.pass = pass;
    }

    public void setDBName(String dbname) {
        this.dbname = dbname;
    }

    public String getDBName() {
        return dbname;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void connect(String host, String user, String password, String database) throws SQLException{
        connect(host, user, password, database, 3306);
    }

    public void connect(String host, String user, String password, String database, int port) throws SQLException{
        setHost(host);
        setUsername(user);
        setPassword(password);
        setDBName(database);
        setPort(port);
        connect();
    }

    public void connect() throws SQLException {
    	System.out.println("total"+this);
        if (connection == null) {
            connection = DriverManager.getConnection("jdbc:mysql://" + getHost() + "/" + getDBName() + "?" + "user="
                    + getUsername() + "&password=" + pass);
        	System.out.println("entro"+connection );

        }
    }

    @Override
	public String toString() {
		return "MySql [connection=" + connection + ", host=" + host + ", user="
				+ user + ", pass=" + pass + ", dbname=" + dbname + ", port="
				+ port + ", devices=" + devices + ", projects=" + projects
				+ "]";
	}

	private List<DynaBean> executeQuery(String query) throws SQLException {
        PreparedStatement statement = null;
        List<DynaBean> beans = Collections.emptyList();
        try {
            statement = connection.prepareStatement(query);
            ResultSet result = executeQuery(statement);
            beans = getDynaBeans(result);
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            if (statement != null) {
                statement.close();
            }
        }

        return beans;
    }

    private ResultSet executeQuery(PreparedStatement statement) throws SQLException {
        ResultSet executeQuery = statement.executeQuery();
        return executeQuery;
    }

    private List<DynaBean> getDynaBeans(ResultSet result) throws Exception {
        ResultSetDynaClass rsdc = new ResultSetDynaClass(result);

        BasicDynaClass bdc = new BasicDynaClass("QueryResult", BasicDynaBean.class, rsdc.getDynaProperties());

        List<DynaBean> dynabeans = new ArrayList<DynaBean>();

        Iterator<DynaBean> rows = rsdc.iterator();
        while (rows.hasNext()) {
            DynaBean newInstance = bdc.newInstance();
            PropertyUtils.copyProperties(newInstance, rows.next());
            dynabeans.add(newInstance);
        }

        return dynabeans;
    }

    public List<Device> getDevices() throws SQLException {
        List<DynaBean> executeQuery = executeQuery("SELECT * FROM DEVICE WHERE (FLAGS & 1) = 1 AND (FLAGS & 8) = 8 AND (FLAGS & 0x20)=0");
        devices=new ArrayList<Device>();
        for (DynaBean bean : executeQuery) {
            Device d = getDevice(bean);
            if (d != null) {
                devices.add(d);
                System.out.println("d"+d);
            }
        }
        return devices;
    }
    
    public List<DynaBean> getQueryResult(String query) throws SQLException{
    	List<DynaBean> executeQuery = executeQuery(query);
        
        return executeQuery;
    }

    public List<Project> getProjects() throws SQLException {
        List<DynaBean> executeQuery = executeQuery("SELECT * FROM PROJECT");
        projects=new ArrayList<Project>();
        for (DynaBean bean : executeQuery) {
            Project p = getProject(bean);
            if (p != null) {
                
                projects.add(p);
            }
        }
        return projects;

    }
    private Project getProject(DynaBean bean){
    	Project project=new Project((String) bean.get("name"));
    	System.out.println("name"+project.getName());
    	//project.setCredentials(bean.get(""));
//    	Field[] fields=project.getClass().getDeclaredFields();
//    	for(Field field:fields){
//    		field.getName();
//    		field.set(long, bean.get("ID"););
//    	}
    	try {
        	long id=(long) bean.get("id");
//        	Field[] fields =projects.getClass().getDeclaredFields();
//        	for(Field field:fields){
//        		System.out.println("field : "+field.getName());
//        	}
//        	
//        	fields =Project.class.getFields();
//        	System.out.println("simples fields "+Arrays.toString(fields));
//        	for(Field field:fields){
//        		System.out.println("field simple : "+field.getName());
//        	}
//        	
//        	fields =Project.class.getDeclaredFields();
//        	System.out.println("simples fields "+Arrays.toString(fields));
//        	for(Field field:fields){
//        		System.out.println("field simple : "+field.getName());
//        	}
        	Field privateId=Project.class.getDeclaredField("id");
        	privateId.setAccessible(true);
        	privateId.setLong(project, (long) bean.get("id"));
        	privateId=Project.class.getDeclaredField("deviceConfigPeriod");
        	privateId.setAccessible(true);
        	privateId.set(project,(String)bean.get("deviceconfigperiod"));
//        	projects.getClass().getDeclaredField("id").setAccessible(true);
//			projects.getClass().getDeclaredField("id").set(id, bean.get("id"));
        	project.setTicketQueryUrl((String) bean.get("ticketqueryurl"));
        	project.setRedundantNmsAvailable((boolean) bean.get("redundantnmsavailable"));
        	
        	project.setStatisticsUrlExternal((String) bean.get("statisticsurlexternal"));
        	project.setRedundantNmsAddress((String) bean.get("redundantnmsaddress"));
        	project.setMaxTimeToReopenAlert((int) bean.get("maxtimetoreopenalert"));

//        	project.setDeviceConfDay(deviceConfDay);
//        	project.setDeviceConfHr(deviceConfHr);
//        	project.setDeviceConfType(deviceConfType);
        	
        	project.setClientInterfaceMandatory((boolean) bean.get("clientinterfacemandatory"));
        	project.setTicketQueryUrlExternal((String) bean.get("ticketqueryurlexternal"));
//        	project.setName((String) bean.get("NAME"));
        	project.setStatisticsUrl((String) bean.get("statisticsurl"));
        	
        	project.setTicketCreationUrl((String) bean.get("ticketcreationurl"));
        	//bean.get("runningconfcommand");
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};
    	
    	
    	return project;
    }
    private Device getDevice(DynaBean bean) {
        Device d = null;
        try {
            d = new Device((String) bean.get("ip"));
        } catch (Exception e) {
            return d;
        }
        d.setConnectionProtocolType(CLIValues[(Integer) bean.get("protocoltype")]);
        d.setType(TypeValues[(Integer) bean.get("type")]);
        d.setSnmpVersion(SnmpVersionValues[(Integer) bean.get("snmpversion")]);
        Field[] fields = Device.class.getDeclaredFields();
        Column annotation = null;
        for (DynaProperty p : bean.getDynaClass().getDynaProperties()) {
            for (Field f : fields) {
                try {
                    annotation = (Column) f.getAnnotations()[0];
                } catch (Exception e) {
                    annotation = null;
                }
                if (f.getName().equalsIgnoreCase(p.getName())
                        || (annotation != null && annotation.name().equalsIgnoreCase(p.getName()))) {
                    f.setAccessible(true);
                    try {
                        f.set(d, bean.get(p.getName()));
                    } catch (IllegalArgumentException e) {
                        // e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }

                }

                annotation = null;
            }
        }

        return d;
    }

    public boolean isConnected() {
        return connection != null;
    }

//    public static MySql getInstance() {
//        return INSTANCE;
//    }
    
    public static void main(String[] args) throws SQLException {
//		MySql.getInstance().connect("192.168.207.41", "root", "vitalnoc", "nms");
    	MysqlCredential mscredn=new MysqlCredential("192.168.208.3", "root", "root", "nms",3306);

    	MySql mysql=new MySql(mscredn);
    	
//    	mysql.connect("192.168.208.3", "root", "root", "nms");
    	mysql.connect();
//    	MySql sql = MySql.getInstance();
    	;
    	System.out.println(mysql.getDevices().size());
    	Device d = mysql.devices.get(10);
    	d.getSysObjectID();
    	System.out.println(d.getSysObjectID());
    	System.out.println(d);
//    	Driver driver = com.gdc.nms.server.drivers.snmp.DriverManager.getInstance().getDriver(d);
//    	try {
//    		for(DeviceResource r : driver.getDeviceResources()){
//    			System.out.println(r.getName());
//    		}
//		} catch (SnmpConnectorException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}finally{
//	    	driver.unbind();
//		}
    	
    	
	}
}

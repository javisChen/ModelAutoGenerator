package constants;

public class PathConst {

	/** 项目根路径 */
	public static final String ROOT_PATH = System.getProperty("user.dir");
	/** 实体类存放位置 */
	public static final String SAVE_ENTITY_PATH = ROOT_PATH + "/src/com/szyt/web/vo";
	/** 数据访问接口存放位置 */
	public static final String SAVE_IDAO_PATH = ROOT_PATH + "/src/com/szyt/web/dao/api";
	/** MybatisMapper存放位置 */
	public static final String SAVE_MAPPER_PATH = ROOT_PATH + "/src/com/szyt/web/dao/mapper";
	/** 业务接口存放位置 */
	public static final String SAVE_ISERVICE_PATH = ROOT_PATH + "/src/com/szyt/web/service/api";
	/** 业务实现类存放位置 */
	public static final String SAVE_SERVICE_PATH = ROOT_PATH + "/src/com/szyt/web/service/impl";
}

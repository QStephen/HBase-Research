���̷��������Ⱥ�˳��

1 HMaster���캯��

  HRegionServer���캯��, HRegionServer��RPC�˿�Ĭ����60020��master��RPC�˿�Ĭ����60000
  HRegionServer��Jetty(InfoServer)�˿�Ĭ����60030��master��Jetty(InfoServer)�˿�Ĭ����60010

  1.1 ��ȡ��ǰ����HMaster�Ļ����ĵ�ַ
  1.2 ����HBaseServer�������ڽ���RPC���󣬲�����HBaseServer������߳�
  1.3 ����ZooKeeperWatcher����
      �ڹ��캯����������Щ�־ý��: /hbase, /hbase/unassigned, /hbase/rs, /hbase/table, /hbase/splitlog

	ZooKeeperWatcher��������10�����:

	baseZNode              "/hbase"
	rootServerZNode        "/hbase/root-region-server"
	rsZNode                "/hbase/rs"
	drainingZNode          "/hbase/draining"
	masterAddressZNode     "/hbase/master"
	clusterStateZNode      "/hbase/shutdown"
	assignmentZNode        "/hbase/unassigned"
	tableZNode             "/hbase/table"
	clusterIdZNode         "/hbase/hbaseid"
	splitLogZNode          "/hbase/splitlog"
	schemaZNode            "/hbase/schema"

	��6�������ZooKeeperWatcher���캯��������
	baseZNode              "/hbase"
	rsZNode                "/hbase/rs"
	drainingZNode          "/hbase/draining"
	assignmentZNode        "/hbase/unassigned"
	tableZNode             "/hbase/table"
	splitLogZNode          "/hbase/splitlog"
	schemaZNode            "/hbase/schema"

	��4���ڲ�ͬ�ط�����
	rootServerZNode        "/hbase/root-region-server"
	masterAddressZNode     "/hbase/master" //��HMaster�н�����������һ�����ݽ�㣬����ֵ��HMaster��ServerName
	                                       //��org.apache.hadoop.hbase.master.ActiveMasterManager.blockUntilBecomingActiveMaster
	clusterStateZNode      "/hbase/shutdown"
	clusterIdZNode         "/hbase/hbaseid" //��HMaster.finishInitialization�����е���ClusterId.setClusterId���������ֵ��UUID

  1.4 ����MasterMetrics����

2 HMaster.run
  
  2.1
  ����ActiveMasterManager���������HMaster��Ϊһ������(backup)��
  ��ô��Ҫ�ȵ���Ⱥ����Active Masterʱ�����µ���blockUntilBecomingActiveMaster��
  ���ҵ���blockUntilBecomingActiveMasterҲ��������ֱ�������ActiveMaster��

  ���ͬʱ����blockUntilBecomingActiveMaster�лᴴ�����ݽ��"/hbase/master"��
  �˽ڵ��ֵ��HMaster�İ汾��ServerName(Ҳ����version+ServerName)��
  �˽������Э��region server��������ֻ��"/hbase/master"�����ú�region server�������½��С�
 
  2.2
  ����HMaster.finishInitialization

    2.2.1
	����MasterFileSystem����
	������hbase-site.xml��hbase.rootdir����ָ����Ŀ¼(��:file:/E:/hbase/data)
	����FSUtils.setVersion��hbase.rootdirĿ¼�н���һ��hbase.version�ļ�����д��汾��(HConstants.FILE_SYSTEM_VERSION=7)
	�ж�-ROOT-�����Ƿ���ڣ������������MasterFileSystem.bootstrap������-ROOT-��.META.

	��󴴽�file:/E:/hbase/data/.oldlogsĿ¼

	2.2.2
	����־ý��"/hbase/hbaseid"�������򴴽��������򲻴�����ͬʱÿ��master����ʱ����Ѵ˽ڵ��ֵ��Ϊhbase.id�ļ��е�ֵ

	2.2.3
	����ExecutorService (TODO)

	2.2.4
	����ServerManager (TODO)

	2.2.5
	initializeZKBasedSystemTrackers

	  2.2.5.1
	  ����CatalogTracker, ����������ZooKeeperNodeTracker���ֱ���RootRegionTracker��MetaNodeTracker��
	  ��Ӧ/hbase/root-region-server��/hbase/unassigned/1028785192���������(1028785192��.META.�ķ�����)
	  ���֮ǰ��δ������hbase����ô��start CatalogTrackerʱ��������㲻���ڡ�
	  /hbase/root-region-server��һ���־ý�㣬��RootLocationEditor�н���

	  2.2.5.2
	  ����AssignmentManager 

	  2.2.5.3
	  ���� LoadBalancer

	  2.2.5.4
	  ���� RegionServerTracker: ���"/hbase/rs"���

	  2.2.5.5
	  ���� DrainingServerTracker: ���"/hbase/draining"���

	  2.2.5.6
	  ���� ClusterStatusTracker��ͨ������setClusterUp���������־ý��"/hbase/shutdown"�����ֵ�ǵ�ǰʱ�䣬
	  �������Ѵ���(master����δ�����ر�)����ô�˽���ֵ�����¡�

	
	2.2.6
	���� MasterCoprocessorHost

	2.2.7
	startServiceThreads()

	���������߳�
	(MASTER_OPEN_REGION��MASTER_CLOSE_REGION��MASTER_SERVER_OPERATIONS��MASTER_META_SERVER_OPERATIONS��MASTER_TABLE_OPERATIONS
	�⼸��ֻ������Executor����δ��ʽ����, ��ʽ��������LogCleaner���ͻ���Jetty��InfoServer(�˿ں�Ĭ����60010))
	
	2.2.8
	�ȴ�RegionServerע��

	2.2.9
	splitLogAfterStartup (TODO)

	2.2.10
	assignRootAndMeta
		
		2.2.10.1
		processRegionInTransitionAndBlockUntilAssigned
		�ȿ�һ�·�������ת��״̬���У��������ת��״̬�������ȴ�����ص�״̬�����ȴ��崦������������½��С�

		2.2.10.2
		verifyRootRegionLocation

		2.2.10.3
		getRootLocation

		2.2.10.4.A
		expireIfOnline

		2.2.10.4.B
		assignRoot

		��ɾ��"/hbase/root-region-server",�������治���ڣ�KeeperException.NoNodeException��������

		д��EventType.M_ZK_REGION_OFFLINE����ǰʱ�������������(-ROOT-,,0)��master�İ汾��ServerName
		��/hbase/unassigned/70236052, payloadΪnull�����Բ�д��

		RegionServer�޸�/hbase/unassigned/70236052��ֵ��
		д��EventType.RS_ZK_REGION_OPENING����ǰʱ�������������(-ROOT-,,0)��RegionServer�İ汾��ServerName

	
	2.2.11
	MetaMigrationRemovingHTD.updateMetaWithNewHRI

	2.2.12
	assignmentManager.joinCluster()
	��meta���еķ�����������Ȼ����䵽Region Server,
	meta��ֻ��һ�����壺info������meta����������: 
	regioninfo��server��serverstartcode��
	����regioninfo��ӦHRegionInfo��
	server��ӦServerName��host��port
	serverstartcode��ӦServerName��startcode(һ����ʱ���)��

		2.2.12.1
		rebuildUserRegions()
			
			2.2.12.1.1
			����MetaReader.fullScan ��meta����ȡ�����еķ������õ�һ��List<Result>��
			����MetaReader.parseCatalogResult������ÿ��result�õ�Pair<HRegionInfo, ServerName>��
			����HRegionInfo��regioninfo�е�ֵ�����л�������ServerName��server��serverstartcode���е�ֵ�����л�����϶��ɡ�

		2.2.12.2
		processDeadServersAndRegionsInTransition


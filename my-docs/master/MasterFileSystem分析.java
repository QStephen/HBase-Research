1.
��HMaster.finishInitialization�����д���MasterFileSystem�Ĺ��캯��

2. MasterFileSystem�Ĺ��캯��������:
   
   hbase-site.xml�ļ��ж�����һ��"hbase.rootdir"���ԣ�������Բ���ȱ�٣�
   �����������Ĵ�:
   java.lang.IllegalArgumentException: Can not create a Path from a null string
	at org.apache.hadoop.fs.Path.checkPathArg(Path.java:75)
	at org.apache.hadoop.fs.Path.<init>(Path.java:85)
	at org.apache.hadoop.hbase.util.FSUtils.getRootDir(FSUtils.java:451)


	MasterFileSystem�Ĺ��캯������ȡ��������Ե�ֵ�����浽Path rootdir����ֶ���

	����windows�µ���������:
	<property>
		<name>hbase.rootdir</name>
		<value>file:/E:/hbase/data</value>
	</property>


	Ȼ���մ˱����͵�ǰ����(Configuration)���õ�һ��org.apache.hadoop.fs.FileSystem
	���û�õ�HDFS��������ͻ���org.apache.hadoop.fs.LocalFileSystem��
	Ȼ����ݴ�FileSystem�õ�File System Uri,���������FileSystem��"file:///"�������ֵ��
	�浽Configuration������������(�ֱ���"fs.default.name"��"fs.defaultFS",��������ļ������У����ｫ�Ḳ����)


	���Ŷ�ȡ"hbase.master.distributed.log.splitting"���Ե�ֵ��Ĭ��distributedLogSplittingΪtrue��
	��ʾ����SplitLogManager������һ��SplitLogManagerʵ��:


	createInitialFileSystemLayout()
		
		checkRootDir

			1. �ȴ�fs�˳���ȫģʽ(Ĭ��10������ѭһ�Σ���ͨ������hbase.server.thread.wakefrequency����

			2.a. ���hbase.rootdirĿ¼�������򴴽�����
			     Ȼ���ڴ�Ŀ¼�д�����Ϊ"hbase.version"���ļ����������ļ�ϵͳ�汾��("7")

			2.b. ���hbase.rootdirĿ¼�Ѵ��ڣ������"hbase.version"�ļ��������뵱ǰ�İ汾����ȣ�
			�������ȣ����ӡ������Ϣ(��ʾ�汾����)���׳��쳣FileSystemVersionException

			3. ���hbase.rootdirĿ¼���Ƿ�����Ϊ"hbase.id"���ļ������û���򴴽�����
			������������ɵ�UUID(�ܳ���36λ����5������ɣ���"-"�ָ�)����: 6c43f934-37a2-4cae-9d49-3f5abfdc113d

			4. ����"hbase.id"���ļ������ݴ浽clusterId�ֶ�

			5. �ж�hbase.rootdirĿ¼���Ƿ���"-ROOT-/70236052"Ŀ¼��û�еĻ�˵���ǵ�һ������hbase������:

				5.1 bootstrap(final Path rd, final Configuration c)

					����HRegion.createHRegion����"-ROOT-"������".META."����ʱ��Ŀ¼��������
					E:\HBASE\DATA
					��  .hbase.id.crc
					��  .hbase.version.crc
					��  hbase.id
					��  hbase.version
					��
					����-ROOT-
					��  ����70236052
					��      ��  ..regioninfo.crc
					��      ��  .regioninfo
					��      ��
					��      ����.logs
					��      ��      .hlog.1329045483158.crc
					��      ��      hlog.1329045483158
					��      ��
					��      ����.oldlogs
					��      ����info
					����.META
						����1028785192
							��  ..regioninfo.crc
							��  .regioninfo
							��
							����.logs
							��      .hlog.1329045485940.crc
							��      hlog.1329045485940
							��
							����.oldlogs
							����info

					��".META."������Ϣ�ӵ�"-ROOT-"�����رշ�����hlogʱ
					E:\HBASE\DATA
					��  .hbase.id.crc
					��  .hbase.version.crc
					��  hbase.id
					��  hbase.version
					��
					����-ROOT-  //"-ROOT-"����
					��  ����70236052 //"-ROOT-"������
					��      ��  ..regioninfo.crc
					��      ��  .regioninfo //"-ROOT-"�����������
					��      ��
					��      ����.oldlogs
					��      ��      .hlog.1329045483158.crc
					��      ��      hlog.1329045483158
					��      ��
					��      ����.tmp
					��      ����info  //������
					��              .c4d7a00bb555409f9a4a8b4fbc57f1bd.crc
					��              c4d7a00bb555409f9a4a8b4fbc57f1bd       //���".META."������Ϣ��StoreFile
					��
					����.META
						����1028785192
							��  ..regioninfo.crc
							��  .regioninfo
							��
							����.oldlogs
							��      .hlog.1329045485940.crc
							��      hlog.1329045485940
							��
							����info



				5.2  createRootTableInfo ����"-ROOT-"��������ļ�

					�ж�hbase.rootdir/-ROOT-Ŀ¼���Ƿ����.tableinfo��ͷ���ļ�

					
			6. ����file:/E:/hbase/data/.oldlogsĿ¼


	ִ����MasterFileSystem���캯��ʱ��Ŀ¼�ṹ����:

		E:\HBASE\DATA
		��  .hbase.id.crc
		��  .hbase.version.crc
		��  hbase.id
		��  hbase.version
		��
		����-ROOT-
		��  ��  ..tableinfo.0000000001.crc
		��  ��  .tableinfo.0000000001
		��  ��
		��  ����.tmp
		��  ����70236052
		��      ��  ..regioninfo.crc
		��      ��  .regioninfo
		��      ��
		��      ����.oldlogs
		��      ��      .hlog.1329045483158.crc
		��      ��      hlog.1329045483158
		��      ��
		��      ����.tmp
		��      ����info
		��              .c4d7a00bb555409f9a4a8b4fbc57f1bd.crc
		��              c4d7a00bb555409f9a4a8b4fbc57f1bd
		��
		����.META
		��  ����1028785192
		��      ��  ..regioninfo.crc
		��      ��  .regioninfo
		��      ��
		��      ����.oldlogs
		��      ��      .hlog.1329045485940.crc
		��      ��      hlog.1329045485940
		��      ��
		��      ����info
		����.oldlogs



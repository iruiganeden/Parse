package parse;





import java.lang.reflect.Field;
import java.util.Set;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.DefaultMessagePrinter;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageEvent;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageListener;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePool;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessagePrinter.*;
import jp.ac.osaka_u.ist.sel.metricstool.main.io.MessageSource;

public class JParse extends MetricsTool{

	/**
	 * JavaソースコードをXMLへ変換
	 */
	public static void main(String[] args) {
		
		doSettings();
		
		final JParse jparse = new JParse();
		jparse.analyzeLibraries();
		jparse.readTargetFiles();
		jparse.analyzeTargetFiles();
		
		final Set<TargetMethodInfo> methods = DataManager.getInstance().getMethodInfoManager().getTargetMethodInfos();
		for(TargetMethodInfo methodInfo : methods){
			String methodName = methodInfo.getOwnerClass().getClassName() + "_" + methodInfo.getMethodName() + ".txt";
			System.out.println(methodInfo.getMethodName());
		}
		
	}

	private static void doSettings() {
		Settings.getInstance().setLanguage("java");
		Settings.getInstance().setTargetDirectory("/home5/akifumi/Parse/Parser");
		Settings.getInstance().setVerbose(true);
		Settings.getInstance().addLibrary("/home5/akifumi/masu/bin/resource/jdk160.jar");
		
		try{
			final Class<?> metricstool = MetricsTool.class;
			final Field out = metricstool.getDeclaredField("out");
			out.setAccessible(true);
			out.set(null, new DefaultMessagePrinter(
					new MessageSource() {
						public String getMessageSourceName() {
							return null;
						}
					}, MESSAGE_TYPE.OUT));
			MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(
					new MessageListener() {
						public void messageReceived(MessageEvent event) {
							System.out.println(event.getSource().getMessageSourceName() + ">" + event.getMessage());
				}
			}
					);
		}catch(SecurityException e){
			e.printStackTrace();
		}catch(NoSuchFieldException e){
			e.printStackTrace();
		}catch(IllegalArgumentException e){
			e.getStackTrace();
		}catch(IllegalAccessException e){
			e.getStackTrace();
		}
	}

}

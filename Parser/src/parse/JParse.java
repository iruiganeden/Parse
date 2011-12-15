package parse;

import java.lang.reflect.Field;
import java.util.Set;
import java.util.SortedSet;

import javax.script.SimpleScriptContext;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.WhileBlockInfo;
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
//			String methodName = methodInfo.getOwnerClass().getClassName() + "_" + methodInfo.getMethodName() + ".txt";
			System.out.println(methodInfo.getMethodName());
			SortedSet<StatementInfo> statements = methodInfo.getStatementsWithSubsequencialBlocks();
			for(StatementInfo statement : statements){
//				System.out.println(statement);
				if(statement instanceof SingleStatementInfo){
					System.out.println(statement);
					if(statement instanceof AssertStatementInfo){	
						System.out.println("Assert");
					}else if(statement instanceof ExpressionStatementInfo){
						System.out.println("Expression");
					}else if(statement instanceof JumpStatementInfo){
						System.out.println("Jump");
						if(statement instanceof BreakStatementInfo){
							System.out.println("Break");
						}else if(statement instanceof ContinueStatementInfo){
							System.out.println("Continue");
						}else{
							System.out.println("Jump未設定");
						}
					}else if(statement instanceof ReturnStatementInfo){
						System.out.println("Return");
					}else if(statement instanceof ThrowStatementInfo){
						System.out.println("Throw");
					}else if(statement instanceof VariableDeclarationStatementInfo){
						System.out.println("VariableDeclaration");
					}else{
						System.out.println("SingleStatement未設定");
					}
				}else if(statement instanceof BlockInfo){
					System.out.println(statement);
					if(statement instanceof CatchBlockInfo){
						System.out.println("CatchBlock");
					}else if(statement instanceof ConditionalBlockInfo){
						System.out.println("ConditionalBlock");
						if(statement instanceof DoBlockInfo){
							System.out.println("DoBlock");
						}else if(statement instanceof ForBlockInfo){
							System.out.println("ForBlock");
						}else if(statement instanceof ForeachBlockInfo){
							System.out.println("ForEachBlock");
						}else if(statement instanceof IfBlockInfo){
							System.out.println("IfBlock");
						}else if(statement instanceof SwitchBlockInfo){
							System.out.println("SwitchBlock");
						}else if(statement instanceof WhileBlockInfo){
							System.out.println("WhileBlock");
						}else{
							System.out.println("ConditionalBlock未設定");
						}
					}else if(statement instanceof ElseBlockInfo){
						System.out.println("ElseBlock");
					}else if(statement instanceof FinallyBlockInfo){
						System.out.println("FinallyBlock");
					}else if(statement instanceof SimpleBlockInfo){
						System.out.println("SimpleBlock");
					}else if(statement instanceof SynchronizedBlockInfo){
						System.out.println("SynchronizedBlock");
					}else if(statement instanceof TryBlockInfo){
						System.out.println("TryBlock");
					}else{
						System.out.println("Block未設定");
					}
				}else if(statement instanceof CaseEntryInfo){
					System.out.println("CaseEnty");
				}else if(statement instanceof DefaultEntryInfo){
					System.out.println("DefaultEntry");
				}else if(statement instanceof LabelInfo){
					System.out.println("Label");
				}else{
					System.out.println("Statement未設定");
				}
			}
		}
		
	}

	private static void doSettings() {
		Settings.getInstance().setLanguage("java");
		Settings.getInstance().setTargetDirectory("/home5/akifumi/git/Parse/Parser/sample");
		Settings.getInstance().setVerbose(true);
		Settings.getInstance().addLibrary("/home5/akifumi/masu/bin/resource/jdk160.jar");
		
//		try{
//			final Class<?> metricstool = MetricsTool.class;
//			final Field out = metricstool.getDeclaredField("out");
//			out.setAccessible(true);
//			out.set(null, new DefaultMessagePrinter(
//					new MessageSource() {
//						public String getMessageSourceName() {
//							return null;
//						}
//					}, MESSAGE_TYPE.OUT));
//			MessagePool.getInstance(MESSAGE_TYPE.OUT).addMessageListener(
//					new MessageListener() {
//						public void messageReceived(MessageEvent event) {
//							System.out.println(event.getSource().getMessageSourceName() + ">" + event.getMessage());
//				}
//			}
//					);
//		}catch(SecurityException e){
//			e.printStackTrace();
//		}catch(NoSuchFieldException e){
//			e.printStackTrace();
//		}catch(IllegalArgumentException e){
//			e.getStackTrace();
//		}catch(IllegalAccessException e){
//			e.getStackTrace();
//		}
	}

}

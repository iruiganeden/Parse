package parse;

import java.util.Set;
import java.util.SortedSet;

import jp.ac.osaka_u.ist.sel.metricstool.main.MetricsTool;
import jp.ac.osaka_u.ist.sel.metricstool.main.Settings;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.DataManager;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayElementUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayInitializerInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ArrayTypeReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.AssertStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BinominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.BreakStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CaseEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CastUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.CatchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ClassReferenceInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ConditionalBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ContinueStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DefaultEntryInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.DoBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ElseBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.EmptyExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ExpressionStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.FinallyBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ForeachConditionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.IfBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.JumpStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LabelInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.LiteralUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MethodCallInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.MonominalOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.NullUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ParenthesesExpressionInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ReturnStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SimpleBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SingleStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.StatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SwitchBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.SynchronizedBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TargetMethodInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TernaryOperationInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.ThrowStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.TryBlockInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.UnknownEntityUsageInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableDeclarationStatementInfo;
import jp.ac.osaka_u.ist.sel.metricstool.main.data.target.VariableUsageInfo;
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
//					System.out.println(statement);
					if(statement instanceof AssertStatementInfo){	
						System.out.println("Assert");
						System.out.println(statement.getText());
					}else if(statement instanceof ExpressionStatementInfo){
//						System.out.println("Expression");
//						System.out.println(((ExpressionStatementInfo) statement).getExpression());
						ExpressionInfo expressionInfo = ((ExpressionStatementInfo) statement).getExpression();
						if(expressionInfo instanceof ArrayElementUsageInfo){
							System.out.println("ArrayElement");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof ArrayInitializerInfo){
							System.out.println("ArrayInitializer");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof ArrayTypeReferenceInfo){
							System.out.println("ArrayType");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof BinominalOperationInfo){
							System.out.println("Binominal");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof CallInfo){
							System.out.println("Call");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof CastUsageInfo){
							System.out.println("Castusage");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof ClassReferenceInfo){
							System.out.println("ClassReference");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof EmptyExpressionInfo){
							System.out.println("EmptyExpression");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof ForeachConditionInfo){
							System.out.println("ForeachCondition");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof LiteralUsageInfo){
							System.out.println("Literal");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof MonominalOperationInfo){
							System.out.println("Monominal");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof NullUsageInfo){
							System.out.println("Null");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof ParenthesesExpressionInfo){
							System.out.println("Parentheses");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof TernaryOperationInfo){
							System.out.println("Ternary");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof UnknownEntityUsageInfo){
							System.out.println("Unknow");
							System.out.println(statement.getText());
						}else if(expressionInfo instanceof VariableUsageInfo){
							System.out.println("Variable");
							System.out.println(statement.getText());
						}else{
							System.out.println("ExpressionStatemen未設定");
						}
					}else if(statement instanceof JumpStatementInfo){
//						System.out.println("Jump");
						if(statement instanceof BreakStatementInfo){
							System.out.println("Break"); 
							System.out.println(statement.getText());
						}else if(statement instanceof ContinueStatementInfo){
							System.out.println("Continue");
							System.out.println(statement.getText());
						}else{
							System.out.println("Jump未設定");
						}
					}else if(statement instanceof ReturnStatementInfo){
						System.out.println("Return");
						System.out.println(statement.getText());
					}else if(statement instanceof ThrowStatementInfo){
						System.out.println("Throw");
						System.out.println(statement.getText());
					}else if(statement instanceof VariableDeclarationStatementInfo){
						System.out.println("VariableDeclaration");
						System.out.println(statement.getText());
						System.out.println(((VariableDeclarationStatementInfo) statement).getInitializationExpression());
					}else{
						System.out.println("SingleStatement未設定");
					}
				}else if(statement instanceof BlockInfo){
//					System.out.println(statement);
					if(statement instanceof CatchBlockInfo){
						System.out.println("CatchBlock");
						System.out.println(statement.getText());
					}else if(statement instanceof ConditionalBlockInfo){
//						System.out.println("ConditionalBlock");
						if(statement instanceof DoBlockInfo){
							System.out.println("DoBlock");
							System.out.println(statement.getText());
						}else if(statement instanceof ForBlockInfo){
							System.out.println("ForBlock");
							System.out.println(statement.getText());
						}else if(statement instanceof ForeachBlockInfo){
							System.out.println("ForEachBlock");
							System.out.println(statement.getText());
						}else if(statement instanceof IfBlockInfo){
							System.out.println("IfBlock");
							System.out.println(statement.getText());
						}else if(statement instanceof SwitchBlockInfo){
							System.out.println("SwitchBlock");
							System.out.println(statement.getText());
						}else if(statement instanceof WhileBlockInfo){
							System.out.println("WhileBlock");
							System.out.println(statement.getText());
						}else{
							System.out.println("ConditionalBlock未設定");
						}
					}else if(statement instanceof ElseBlockInfo){
						System.out.println("ElseBlock");
						System.out.println(statement.getText());
					}else if(statement instanceof FinallyBlockInfo){
						System.out.println("FinallyBlock");
						System.out.println(statement.getText());
					}else if(statement instanceof SimpleBlockInfo){
						System.out.println("SimpleBlock");
						System.out.println(statement.getText());
					}else if(statement instanceof SynchronizedBlockInfo){
						System.out.println("SynchronizedBlock");
						System.out.println(statement.getText());
					}else if(statement instanceof TryBlockInfo){
						System.out.println("TryBlock");
						System.out.println(statement.getText());
					}else{
						System.out.println("Block未設定");
					}
				}else if(statement instanceof CaseEntryInfo){
					System.out.println("CaseEnty");
					System.out.println(statement.getText());
				}else if(statement instanceof DefaultEntryInfo){
					System.out.println("DefaultEntry");
					System.out.println(statement.getText());
				}else if(statement instanceof LabelInfo){
					System.out.println("Label");
					System.out.println(statement.getText());
				}else{
					System.out.println("Statement未設定");
				}
			}
		}
		
	}
	
	private static void statementAnalysis(StatementInfo statement){
		
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

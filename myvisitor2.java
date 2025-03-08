import minipython.analysis.*;
import minipython.node.*;
import java.util.*;
@SuppressWarnings("unchecked")

public class myvisitor2 extends DepthFirstAdapter 
{
	private Hashtable symtable;
	public static int errors;
	boolean in_func;	//used to check if it is inside function declaration
	Node function;
	ArrayList<String> real_args = null; //the real arguments
	ArrayList<String> passed_t_args = null;//the type of the passed arguments
	String type;	//the return type 
	boolean in_fc;
	boolean in_fc1;
	
	
	myvisitor2(Hashtable symtable){
		this.symtable = symtable;
		errors = 0;
		in_fc1 = false;
		in_fc = false;
	}
	
	//check the number of arguments and that function is defined -- No2 -- No3
	@Override
	public void caseAFuncCall(AFuncCall node)
    {
        inAFuncCall(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
		function = null;
		type = null;
		ArrayList<Node> p_args = new ArrayList<Node>(); //use them to store arguments, the passed arguments,
		ArrayList<String> r_args = new ArrayList<String>();//the real argumemts,
		ArrayList<String> p_types = new ArrayList<String>();//the type of the passed arguments
        {
			String id = node.getId().toString();//id

			int par = node.getArglist().size();//number of arguments
			
			if (par==1){//store the given args in the proper list
				p_args.add(((AArglist)node.getArglist().get(0)).getExpression());

				par += ((AArglist)node.getArglist().get(0)).getComExp().size();
				for(int i = 0; i < par-1; i++) {
					p_args.add(((AComExp)((AArglist)node.getArglist().get(0)).getComExp().get(i)).getExpression());
				}
			}
			
			if(!symtable.containsKey(id)) {
				int line = ((TId) node.getId()).getLine();
				System.out.println("Line " +line +": " +"Function " + id +" is not defined.");
			}else{ //if function is defined
				ArrayList<Node> n = (ArrayList)symtable.get(id);
				HashMap<String,PValue> r_def;
				boolean is_def = false; //check that is defined
				boolean ok_args = false; //check that he number of parameters is correct

				for(int i =0; i< n.size(); i++ ) {
					if( n.get(i) instanceof AFunction) {
						r_args = new ArrayList<String>();
						r_def = new HashMap<String,PValue>();
						is_def = true;
						
						AFunction nod = (AFunction)n.get(i);
						int n_par = 0; //number of  parameters
						int def_n_params = 0; //number of def parameters

						if (nod.getArgument().size() == 1) {
							AArgument nArg = (AArgument)nod.getArgument().get(0);
							n_par = 1; //first 

							def_n_params = nArg.getAsValue().size();
							if(def_n_params==1) r_def.put(nArg.getId().toString(), (((AAsValue)nArg.getAsValue().get(0)).getValue())); 
							r_args.add(nArg.getId().toString());

							n_par += nArg.getComIdAsVal().size();//add the others
							for (Object v  : nArg.getComIdAsVal()) {
								def_n_params += ((AComIdAsVal) v).getAsValue().size();
								if(((AComIdAsVal) v).getAsValue().size()==1) r_def.put(((AComIdAsVal)v).getId().toString(), (((AAsValue)((AComIdAsVal)v).getAsValue().get(0)).getValue()));
							    r_args.add(((AComIdAsVal)v).getId().toString());
							}
						}
						if((def_n_params==0 && par==n_par) || (par >= n_par-def_n_params && n_par >= par)) {
							ok_args = true;
							function = ((AFunction)n.get(i));

					
							while (r_args.size() > p_args.size()){
								p_args.add(r_def.get(r_args.get(p_args.size())));
							}
							
							String or_type = null;
							if (p_args != null){
								for(int y = 0; y < p_args.size(); y++) {//get the type of all the given parameters
									if(p_args.get(y) instanceof PExpression) or_type = getType(((PExpression)p_args.get(y)));
									else {
										PValue val = ((PValue)p_args.get(y));
										if(val instanceof ANumbValue) or_type = "NUMBER";
										else if (val instanceof ANoneValue) or_type ="NONE";
										else if (val instanceof AStrValue) or_type = "STRING";
									}
									p_types.add(or_type);
								}
							}
							break;
				}}}
				int line = ((TId) node.getId()).getLine();
				if(!is_def) { //if the function is not defined
					System.out.println("Line " + line+ ": Function " + id +" is not defined.");
					errors++;
				}
				if(!ok_args) {	//if the arguments aren't correct
					System.out.println("Line " +line + ": Function " + id +" with "+par+" arguments is not defined.");
					errors++;
				}
			}
            Object temp[] = node.getArglist().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PArglist) temp[i]).apply(this);
            }
        }
		//return type
		if(function!=null) {
			real_args = r_args;
			passed_t_args = p_types;
			in_fc = true;
			((AFunction)function).apply(this); 
			in_fc = false;
			real_args= null;
			passed_t_args = null;
		}

        outAFuncCall(node);
    }
	
	//method to find the type of an argument
	private String getType(PExpression expression) {	//arithmetic commands
		if (expression instanceof AMinusExpression || expression instanceof APlusExpression || expression instanceof AModExpression 
			|| expression instanceof ADivExpression || expression instanceof AMultExpression || expression instanceof ADmultExpression 
			|| expression instanceof AIncreaseExpression || expression instanceof ADecreaseExpression ||  expression instanceof AParExpression 
			|| expression instanceof AMinExpression || expression instanceof AMaxExpression ){
			return "NUMBER";
		}else if(expression instanceof AValueExpression) { //value
			PValue val = ((AValueExpression)expression).getValue();
			if(val instanceof ANumbValue) return "NUMBER";
			else if (val instanceof ANoneValue) return "NONE";
			else if (val instanceof AStrValue) return "STRING";
		}else if(expression instanceof AListExpression ) {	//list
			String id = ((AListExpression)expression).getId().toString();
			ArrayList<Node> n = (ArrayList)symtable.get(id); 
			AAssignStatement node = null;
			int line = ((AListExpression)expression).getId().getLine(); 
			int line2;
			for(int i = 0; i < n.size(); i++) {
				if(n.get(i) instanceof AAssignStatement){
					line2 = ((AAssignStatement)n.get(i)).getId().getLine();
					if(line2 > line) {
						break;
					} else {
						node = (AAssignStatement)n.get(i);
					}
				}
			}
			return (String)getOut(node);
		}
		else if(expression instanceof AIdExpression) {	//identifier
			String id = ((AIdExpression)expression).getId().toString();
			ArrayList<Node> n = (ArrayList)symtable.get(id); 
			AAssignStatement node = null;
			int line = ((AIdExpression)expression).getId().getLine(); 
			int line2;
			for(int i = 0; i < n.size(); i++) {
				if(n.get(i) instanceof AAssignStatement){
					line2 = ((AAssignStatement)n.get(i)).getId().getLine();
					if(line2 > line){
						break;
					}else {
						node = (AAssignStatement)n.get(i);
					}
			}}
			return (String)getOut(node);
		}
		return null;
	}
	
	//check that in arithmetic commands None is not used -- No 5
	// -- No4 -- No 6 (until the end)
	//get the expression(s) and check it (them)
	@Override
	public void caseADecreaseExpression(ADecreaseExpression node)// --
    {
        inADecreaseExpression(node);
        if(node.getExpression() != null)
        {
			Node e = node.getExpression();
			arithmetic((PExpression)e,"Derease");
            node.getExpression().apply(this);
        }
        outADecreaseExpression(node);
    }
	
	@Override
	public void caseAIncreaseExpression(AIncreaseExpression node)// ++
    {
        inAIncreaseExpression(node);
        if(node.getExpression() != null)
        {
			Node e = node.getExpression();
			arithmetic((PExpression)e,"Increase");
            node.getExpression().apply(this);
        }
        outAIncreaseExpression(node);
    }
	
	@Override
	public void caseADmultExpression(ADmultExpression node)// **
    {
        inADmultExpression(node);
        if(node.getLPar() != null)
        {
			Node l = node.getLPar();
			arithmetic((PExpression)l,"Dmult");
            node.getLPar().apply(this);
        }
        if(node.getRPar() != null)
        {
			Node r = node.getRPar();
			arithmetic((PExpression)r,"Dmult");
            node.getRPar().apply(this);
        }
        outADmultExpression(node);
    }
	
	@Override
	public void caseAMultExpression(AMultExpression node)// *
    {
        inAMultExpression(node);
        if(node.getLPar() != null)
        {
			Node l = node.getLPar();
			arithmetic((PExpression)l,"Mult");
            node.getLPar().apply(this);
        }
        if(node.getRPar() != null)
        {
			Node r = node.getRPar();
			arithmetic((PExpression)r,"Mult");
            node.getRPar().apply(this);
        }
        outAMultExpression(node);
    }
	
	@Override
	public void caseADivExpression(ADivExpression node)// /
    {
        inADivExpression(node);
        if(node.getLPar() != null)
        {
			Node l = node.getLPar();
			arithmetic((PExpression)l,"Div");
            node.getLPar().apply(this);
        }
        if(node.getRPar() != null)
        {
			Node r = node.getRPar();
			arithmetic((PExpression)r,"Div");
            node.getRPar().apply(this);
        }
        outADivExpression(node);
    }
	
	@Override
	public void caseAModExpression(AModExpression node)//%
    {
        inAModExpression(node);
        if(node.getLPar() != null)
        {
			Node l = node.getLPar();
			arithmetic((PExpression)l,"Mod");
            node.getLPar().apply(this);
        }
        if(node.getRPar() != null)
        {
			Node r = node.getRPar();
			arithmetic((PExpression)r,"Mod");
            node.getRPar().apply(this);
        }
        outAModExpression(node);
    }

	@Override
	public void caseAPlusExpression(APlusExpression node)//+
    {
        inAPlusExpression(node);
        if(node.getLPar() != null)
        {
			Node l = node.getLPar();
			arithmetic((PExpression)l,"Plus");
            node.getLPar().apply(this);
        }
        if(node.getRPar() != null)
        {
			Node r = node.getRPar();
			arithmetic((PExpression)r,"Plus");
            node.getRPar().apply(this);
        }
        outAPlusExpression(node);
    }
	
	@Override
	public void caseAMinusExpression(AMinusExpression node)//-
    {
        inAMinusExpression(node);
        if(node.getLPar() != null)
        {
			Node l = node.getLPar();
			arithmetic((PExpression)l,"Minus");
            node.getLPar().apply(this);
        }
        if(node.getRPar() != null)
        {
			Node r = node.getRPar();
			arithmetic((PExpression)r,"Minus");
            node.getRPar().apply(this);
        }
        outAMinusExpression(node);
    }
	
	//through this we check if we are in a function declaration
	@Override
	public void inAFunction(AFunction node)
    {
        in_func = true;
    }
	
	@Override
    public void outAFunction(AFunction node)
    {
        in_func = false;
    }
	
	@Override
	public void caseADeqStatement(ADeqStatement node)// /=
    {
        inADeqStatement(node);
        if(node.getId() != null) {
			node.getId().apply(this);
			String id = node.getId().toString();
			int line = node.getId().getLine(); 
			String g_type = null; 
			ArrayList<Node> nodes = (ArrayList)symtable.get(id); 
			AAssignStatement n = null; 
			int other_line;
			if(in_fc) {
				if(real_args.contains(id)){
					int index = real_args.indexOf(id);
					g_type = passed_t_args.get(index);
			}}
			
			if (g_type==null){
				for(int i = 0; i < nodes.size(); i++) {
					if(nodes.get(i) instanceof AAssignStatement){
						other_line = ((AAssignStatement)nodes.get(i)).getId().getLine();
						if(other_line > line){
							break;
						}else {
							n = (AAssignStatement)nodes.get(i);
						}
						g_type = (String)getOut(n);
					}
				}
			}
			//check the type it returns
			if(in_func && real_args == null) {
				g_type = "NUMBER";
			}else {
				g_type = "NOTNUMBER";
			}
			if(!g_type.equals("NUMBER")){
				System.out.println("Line " + line + ": Div Assign operation must be on numbers only.");
				errors++;
			}
        }
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
        outADeqStatement(node);
    }
	
	@Override
	public void caseAMeqStatement(AMeqStatement node)// -=
    {
        inAMeqStatement(node);
        if(node.getId() != null) {
			node.getId().apply(this);
			String id = node.getId().toString();
			int line = node.getId().getLine(); 
			String g_type = null; // check the type
			ArrayList<Node> nodes = (ArrayList)symtable.get(id); 
			AAssignStatement n = null; 
			int other_line;
			if(in_fc) {
				if(real_args.contains(id)){
					int index = real_args.indexOf(id);
					g_type = passed_t_args.get(index);
			}}
			if (g_type==null){
				for(int i = 0; i < nodes.size(); i++) {
					if(nodes.get(i) instanceof AAssignStatement){
						other_line = ((AAssignStatement)nodes.get(i)).getId().getLine();
						if(other_line > line){
							break;
						}else {
							n = (AAssignStatement)nodes.get(i);
						}
						g_type = (String)getOut(n);
					}
				}
			}
			//check the type it returns
			if(in_func && real_args == null) {
				g_type = "NUMBER";
			}else {
				g_type = "NOTNUMBER";
			}
			if(!g_type.equals("NUMBER")){
				System.out.println("Line " + line + ": Minus Assign operation must be on numbers only.");
				errors++;
			}
        }
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
        outAMeqStatement(node);
    }
	
	//add the type in OUT
	@Override
	public void caseAAssignStatement(AAssignStatement node)
    {
        inAAssignStatement(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        if(node.getExpression() != null)
        {
            node.getExpression().apply(this);
        }
		PExpression exp = node.getExpression();
		String g_type = null; 
		//take the type
		if(exp instanceof AValueExpression) {	//value
			AValueExpression val = (AValueExpression)exp;
			if ( val.getValue() instanceof ANumbValue) g_type = "NUMBER";
			else if(val.getValue() instanceof AStrValue) g_type = "STRING";
			else if(val.getValue() instanceof ANoneValue) g_type = "NONE";
			else if(val.getValue() instanceof AMethodValue) {}
		}else if (exp instanceof AIdExpression) {	//identifier
			ArrayList<Node> nodes = (ArrayList)symtable.get(((AIdExpression)exp).getId().toString());
			AAssignStatement id = null;
			int line = ((AIdExpression)exp).getId().getLine(); 
            int other_line;
			for(int i = 0; i < nodes.size(); i++) {
				if(nodes.get(i) instanceof AAssignStatement){
					other_line = ((AAssignStatement)nodes.get(i)).getId().getLine();
					if(other_line > line) break;
					else id = (AAssignStatement)nodes.get(i);
				}
			}
			g_type = (String)getOut(id);
		}else if(exp instanceof AListExpression) {	//list
			ArrayList<Node> nodes = (ArrayList)symtable.get(((AListExpression)exp).getId().toString());
			AAssignStatement id = null;
			for(int i = 0; i< nodes.size(); i++) {
				if(nodes.get(i) instanceof AAssignStatement) id = (AAssignStatement)nodes.get(i);
			}
			g_type = (String)getOut(id);
		}else if(exp instanceof AFuncCallExpression) {
			g_type = type;
		} else if(exp instanceof APlusExpression || exp instanceof AMinusExpression || exp instanceof ADivExpression 
				|| exp instanceof AModExpression || exp instanceof AMultExpression 
				|| exp instanceof ADmultExpression || exp instanceof AMaxExpression || exp instanceof AMinExpression){
					g_type = "NUMBER";
		}
		//store the type in OUT
		setOut(node,g_type);
        outAAssignStatement(node);
    }
	
	
	// find the return type to use it when the function is called
	@Override
	public void inAReturnStatement(AReturnStatement node) {	//arithmetic commands
		PExpression expression = node.getExpression();
		if (expression instanceof APlusExpression || expression instanceof AMinusExpression || expression instanceof AMultExpression 
			|| expression instanceof ADmultExpression || expression instanceof AModExpression || expression instanceof ADivExpression 
			|| expression instanceof AParExpression || expression instanceof AMinExpression || expression instanceof AMaxExpression){
			type = "NUMBER";
		}else if(expression instanceof AValueExpression) {	//value
			in_fc1 = false;
			PValue val = ((AValueExpression)expression).getValue();
			if(val instanceof ANumbValue) type = "NUMBER";
			else if (val instanceof ANoneValue) type = "NONE";
			else if (val instanceof AStrValue) type = "STRING";
		}else if(expression instanceof AIdExpression || expression instanceof AListExpression) {	//identifier or list
			String id; int line;
			if (expression instanceof AIdExpression) {
				id = ((AIdExpression)expression).getId().toString();
				line = ((AIdExpression)expression).getId().getLine();
			}else {
				id = ((AListExpression)expression).getId().toString();
				line = ((AListExpression)expression).getId().getLine();
			}
			in_fc1 = false;
			ArrayList<Node> nodes = (ArrayList)symtable.get(id); 
			AAssignStatement n = null; 
			int other_line;
			if(in_fc) {
				if(real_args.contains(id)){
					int index = real_args.indexOf(id);
					type = passed_t_args.get(index);
				}
			}
			if(type==null){
				for(int i = 0; i < nodes.size(); i++) {
					if(nodes.get(i) instanceof AAssignStatement){
						other_line = ((AAssignStatement)nodes.get(i)).getId().getLine();
						if(other_line > line) break;
						else n = (AAssignStatement)nodes.get(i);
						type = (String)getOut(n);
		}}}}
	}
	
	//find the return type of the function in print
	//print message if function doesn't return something
	public void caseAPrintStatement(APrintStatement node){
        inAPrintStatement(node);
		int line;
		if(!in_fc1) {
			if(node.getExpression() != null) {
				node.getExpression().apply(this);
				if(node.getExpression() instanceof AFuncCallExpression) {
					if(type == null) {
						line = ((AFuncCall)((AFuncCallExpression)node.getExpression()).getFuncCall()).getId().getLine();
						System.out.println("Line "+line+ ": Function "+((AFuncCall)((AFuncCallExpression)node.getExpression()).getFuncCall()).getId().toString() +" doesn't return something.");
						errors++;
			}}}
			{
				Object temp[] = node.getComExp().toArray();
				for(int i = 0; i < temp.length; i++) {
					((PComExp) temp[i]).apply(this);
					if(((AComExp)((PComExp) temp[i])).getExpression() instanceof AFuncCallExpression) { 
						if(type == null) {
							line = ((AFuncCall)((AFuncCallExpression)((AComExp)((PComExp) temp[i])).getExpression()).getFuncCall()).getId().getLine();
							System.out.println("Line "+line+": Function "+((AFuncCall)((AFuncCallExpression)((AComExp)((PComExp) temp[i])).getExpression()).getFuncCall()).getId().toString() +" doesn't return something.");
							errors++;
				}}}
			}
		}else {
			in_fc1 = false;
		}
		outAPrintStatement(node);
    }
	
	//this checks that in arithmetic expressions are used only numbers
	private void arithmetic(PExpression expression, String operation){
		int other_line=0; 
		String g_type = null; 
		AAssignStatement n; 
		ArrayList<Node> nodes; 
		int line;
		ArrayList<String> real = null; 
		ArrayList<String> passed = null;
		if(expression instanceof AFuncCallExpression) {
			in_fc1 = true;
		}
		expression.apply(this);
		if(in_fc) {//store the arguments
			real = new ArrayList<String>();
			for(String s : real_args) real.add(s);
			passed = new ArrayList<String>();
			for(String no : passed_t_args) passed.add(no);
		}
		if(!(expression instanceof APlusExpression || expression instanceof AMinusExpression || expression instanceof AMultExpression 
			|| expression instanceof ADmultExpression || expression instanceof AModExpression || expression instanceof ADivExpression)){
			if(expression instanceof AParExpression) {	//parenthesis
				PExpression par_exp = ((AParExpression)expression).getExpression();
				if(!(par_exp instanceof APlusExpression || par_exp instanceof AMinusExpression || par_exp instanceof AMultExpression 
				|| par_exp instanceof ADmultExpression || par_exp instanceof AModExpression || par_exp instanceof ADivExpression)) {
					expression = par_exp;
				}
			}
			if(expression instanceof AValueExpression) {	//value
				PValue val = ((AValueExpression)expression).getValue();
				if(val instanceof ANoneValue) {
					line = ((ANoneValue)val).getNone().getLine();
					System.out.println("Line " +line +": "+operation+" operation cannot be done on None.");
					errors++;
				} else if(!(val instanceof ANumbValue)) {
					if(val instanceof AStrValue) {
						line = ((AStrValue)val).getString().getLine();
					}else {
						line = ((AMethodValue)val).getId().getLine();
					}
					System.out.println("Line " + line + ": "+operation+" operation must be on numbers only.");
					errors++;
				}
			}else if(expression instanceof AIdExpression || expression instanceof AListExpression) {	//identifier or list
				String id;
				if (expression instanceof AIdExpression) {
					id = ((AIdExpression)expression).getId().toString();
					line = ((AIdExpression)expression).getId().getLine();
				}else {
					id = ((AListExpression)expression).getId().toString();
					line = ((AListExpression)expression).getId().getLine();
				}
				nodes = (ArrayList)symtable.get(id); 
				n = null;
				if(in_fc) {
					if(real.contains(id)){
						int index = real.indexOf(id);
						g_type = passed.get(index);
					}
				}
				if (g_type==null){
					if (nodes != null){
						for(int i = 0; i < nodes.size(); i++) {
							if(nodes.get(i) instanceof AAssignStatement){
								other_line = ((AAssignStatement)nodes.get(i)).getId().getLine();
								if(other_line > line) break;
								else n = (AAssignStatement)nodes.get(i);
								g_type = (String)getOut(n);
							}
						}
					}
				}

				if(in_func && real_args == null) {
					g_type = "NUMBER";
				}
				if(g_type.equals("NONE")){
					System.out.println("Line " +line+ ": "+operation+" operation cannot be done on None.");
					errors++;
				}else if(!g_type.equals("NUMBER")) {
					System.out.println("Line " +line+ ": "+operation+" operation must be on numbers only.");
					errors++;
				}
			}else if(expression instanceof AFuncCallExpression) {	//function call
				line = ((AFuncCall)((AFuncCallExpression)expression).getFuncCall()).getId().getLine();
				g_type = type;	//save the type
				if(function!=null) {
					if(g_type==null){
						System.out.println("Line " +line+ ": " +"Function "+ ((AFunction)function).getId().toString()+" doesn't return anything.");
						errors++;
					}
					else if(g_type.equals("NONE")) {
						System.out.println("Line " +line+ ": "+operation+" operation cannot be done on None.");
						errors++;
					}else if(!g_type.equals("NUMBER")) {
						System.out.println("Line " +line+ ": "+operation+" operation must be on numbers only.");
						errors++;
					}	
				}
			}
		}
	}

}
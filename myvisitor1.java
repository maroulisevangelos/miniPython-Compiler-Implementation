import minipython.analysis.*;
import minipython.node.*;
import java.util.*;
@SuppressWarnings("unchecked")

public class myvisitor1 extends DepthFirstAdapter 
{
	private Hashtable symtable;
	public static int errors;
	String if_name;	//id used in for command
	ArrayList<String> args;
	boolean inside_func;	//check if it is inside function
	boolean inside_for;		//check if it is inside for

	myvisitor1(Hashtable symtable) 
	{
		this.symtable = symtable;
		errors = 0;
		inside_for = false;
		inside_func = false;
	}
	
	//after assignment add id to the symboltable 
	@Override
	public void inAAssignStatement(AAssignStatement node)
    {
        String id = node.getId().toString();
		if(!symtable.containsKey(id)) {
			symtable.put(id,new ArrayList<Node>(Arrays.asList(node)));
		}
    }
	
	//check if function is defined -- No7
	@Override
	public void caseAFunction(AFunction node)
    {
        inAFunction(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        {
            Object temp[] = node.getArgument().toArray();
			String function_name = node.getId().toString();
			int line = ((TId) node.getId()).getLine();
			inside_func = true;
			int n_par = 0; //number of parameters
			int def_n_par = 0; //number of parameters with default value 
			
			if (((AFunction) node).getArgument().size() == 1) { //count number of parameters
				AArgument args_node = (AArgument) ((AFunction) node).getArgument().get(0);
				n_par = 1;//add the first argument
				def_n_par = args_node.getAsValue().size();
				n_par += args_node.getComIdAsVal().size();//add the other arguments
				for (Object ob  : args_node.getComIdAsVal()) {
					def_n_par += ((AComIdAsVal) ob).getAsValue().size();
				}
			}
			
			boolean free_name = true; //check if the name already exists
			if(symtable.containsKey(function_name)) {
				ArrayList<Node> exist_node = (ArrayList)symtable.get(function_name);
				Node h_node;
				
				for(int i = 0; i < exist_node.size(); i++) { //for every of the same name in symboltable
					h_node = exist_node.get(i);
					
					if (h_node instanceof AFunction){
						int h_n_par = 0; //number of parameters
						int h_def_n_par = 0; //number of parameters with default value
						
						if (((AFunction) h_node).getArgument().size() == 1) { //count number of parameters
							AArgument args_h_node = (AArgument) ((AFunction) h_node).getArgument().get(0);
							h_n_par = 1;//add the first argument
							h_def_n_par = args_h_node.getAsValue().size();
							h_n_par += args_h_node.getComIdAsVal().size(); //add the other arguments
							for (Object ob  : args_h_node.getComIdAsVal()) {
								h_def_n_par += ((AComIdAsVal) ob).getAsValue().size();
							}
						}
						
						if ((n_par - def_n_par <= h_n_par) && (h_n_par - h_def_n_par <= n_par - def_n_par || def_n_par != 0)) {
							System.out.println("Line " + line + ": " +" Function " + function_name +" is already defined.");
							errors++;
							free_name = false;
							break;
						}
					}
				}
			}
			
			if (free_name) { 
				
				if(!symtable.containsKey(function_name)){ //if there is not this function name in the symboltable
					symtable.put(function_name,new ArrayList<Node>(Arrays.asList(node))); //add function in symboltable
				}
			}
			args = new ArrayList<String>();
            for(int i = 0; i < temp.length; i++)
            {
                ((PArgument) temp[i]).apply(this);
            }
			if(node.getStatement() != null)
			{
				node.getStatement().apply(this);
			}
			inside_func = false;
        }
        outAFunction(node);
    }
	
	//store arguments in a list
	@Override
	public void caseAArgument(AArgument node)
    {
        inAArgument(node);
        if(node.getId() != null)
        {
            node.getId().apply(this);
        }
        {
            Object temp[] = node.getAsValue().toArray();
            for(int i = 0; i < temp.length; i++)
            {
                ((PAsValue) temp[i]).apply(this);
            }
        }
        {
			String id = node.getId().toString();
			args.add(id);	//store the first argument
            Object temp[] = node.getComIdAsVal().toArray();
			int line = ((TId) node.getId()).getLine();
			String function_name = ((AFunction)(node.parent())).getId().toString();
			for(int i = 0; i < temp.length; i++) {
				String id_t = ((AComIdAsVal)temp[i]).getId().toString();
				args.add(id_t);	//store the other arguments
			}

            for(int i = 0; i < temp.length; i++)
            {
                ((PComIdAsVal) temp[i]).apply(this);
            }
        }
        outAArgument(node);
    }
	
	//add the the arguments in the symboltable
	@Override
	public void inAComIdAsVal(AComIdAsVal node)
    {
        String argN = node.getId().toString(); 
		if(!symtable.containsKey(argN)){
			symtable.put(argN,new ArrayList<Node>(Arrays.asList(node)));
		}
    }
	
	//add the for_id in symboltable and check if the in_id is defined
	@Override	
	public void caseAForStatement(AForStatement node)
    {
        inAForStatement(node);
		inside_for = true;
		args = new ArrayList<String>();
        if(node.getFId() != null)
        {
            node.getFId().apply(this);
        }
		String fId = node.getFId().toString();
		if(!symtable.containsKey(fId)) {	// add it in the symboltable
			symtable.put(fId,new ArrayList<Node>(Arrays.asList(node)));
		}
        if(node.getIId() != null)
        {
            node.getIId().apply(this);
        }
		String iId = node.getIId().toString();
		if (!symtable.containsKey(iId)) {	//check if it is defined 
			int line = ((TId) node.getIId()).getLine();
			System.out.println("Line " + line + ": " + "Name " + node.getIId().toString() +" is not defined.");
			errors++;
		}
		if_name = fId;
        if(node.getStatement() != null)
        {
            node.getStatement().apply(this);
        }
		inside_for = false;
        outAForStatement(node);
    }
	
	//check if id is not defined  -- No1
	@Override
	public void inAIdExpression(AIdExpression node)
    {
        int line = ((TId) node.getId()).getLine();
		String id = node.getId().toString();
		
		if (!is_defined(id)) {
			System.out.println("Line " + line + ": " +"Name " + id +" is not defined.");
			errors++;
		}
    }
	
	//check it also for -=
	@Override
	public void inAMeqStatement(AMeqStatement node)
    {
        int line = ((TId) node.getId()).getLine();
		String id = node.getId().toString();
		
		if (!is_defined(id)) {
			System.out.println("Line " + line + ": " +"Name " + id +" is not defined.");
			errors++;
		}
    }
	
	//check it also for /=
	@Override
	public void inADeqStatement(ADeqStatement node)
    {
        int line = ((TId) node.getId()).getLine();
		String id = node.getId().toString();
		
		if (!is_defined(id)) {
			System.out.println("Line " + line + ": " +"Name " + id +" is not defined.");
			errors++;
		}
    }
	
	//check it also for list
	@Override
	public void inAListStatement(AListStatement node)
    {
        int line = ((TId) node.getId()).getLine();
		String id = node.getId().toString();
		
		if (!is_defined(id)) {
			System.out.println("Line " + line + ": " +"Name " + id +" is not defined.");
			errors++;
		}
    }
	
	
	//function that checks if this id is in the hashtable
	private boolean is_defined(String id) {
		if(symtable.containsKey(id)) {
			ArrayList<Node> val = (ArrayList)symtable.get(id);
	
			for(int i = 0; i < val.size(); i++) {
				if(val.get(i) instanceof AAssignStatement){
					return true;
				}
			}
		}
		
		//here it checks if this is an argument of a function
		if(args.contains(id) && inside_func){
			return true;
		}
		//here it checks if this is an id in for
		if(id.equals(if_name) && inside_for){
			return true;
		}
		return false;
	}
}

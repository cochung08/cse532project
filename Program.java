
public class Program {
	public static void main (String[] args)
	{
		String data_files = "C:\\Users\\vunguyen\\Documents\\CSE532-Database\\data_files";
		String username = "db2admin";		// You put your username here
		String password = "1234567890";		// You put your password here
		ArticleCollection ac = new ArticleCollection("meddb", username, password);
		ac.readFromTextFiles(data_files, 2);
		//ac.test1();
	}
}

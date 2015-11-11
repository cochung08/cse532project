
public class Program {
	public static void main (String[] args)
	{
		String data_files = "E:\\Study\\PhD\\CSE532-Theory of Database Systems\\Project\\data_files";
		String username = "";		// You put your username here
		String password = "";		// You put your password here
		ArticleCollection ac = new ArticleCollection("meddb", username, password);
		ac.readFromTextFiles(data_files, 2);
		//ac.test1();
	}
}

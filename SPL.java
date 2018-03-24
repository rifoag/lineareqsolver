import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.File;
import java.io.FileNotFoundException;

public class SPL {
	public static void TampilkanMenu() {
		System.out.println("Pemecah Sistem Persamaan Lanjar");
		System.out.println();
		System.out.println("Pilih sumber masukan :");
		System.out.println("1. Masukan dari keyboard");
		System.out.println("2. Masukan dari file eksternal");
		System.out.println("3. Matriks Hilbert");
		System.out.println("4. Interpolasi contoh f");
		System.out.println("5. Interpolasi contoh g");
		System.out.println("6. Interpolasi contoh h");
		System.out.println("7. Interpolasi contoh i");
	}

	public static int maks(int k, int m, double[][] aug){
		//mengeluarkan indeks baris yang menghasilkan aug[i][k] maksimal
		int i, maks;
		double makso;
		makso = aug[k][k];
		maks = k; 
		for (i = k+1;i < m; i = i+1){
			if (Math.abs(aug[i][k]) > makso){
				maks = i;
				makso = Math.abs(aug[i][k]);
			}
		}
		return maks;
	}

	public static void tukarbaris(double[][] aug, int i_maks, int k, int n){
		//menukar baris i_maks dengan baris k
		int i;
		double temp;

		for (i = 0; i <= n; i++){
			temp = aug[i_maks][i];
			aug[i_maks][i] = aug[k][i];
			aug[k][i] = temp;	
		}
	}


	public static void gaussRREF(double[][] aug, int m, int n){
		//I.S : Matriks aug terdefinisi dengan dimensi m x n
		//F.S : Matriks aug menjadi bentuk reduced row echelon
		for (int k = 0; k < m;k = k+1){
			//Pivoting, untuk meminimalkan galat dalam pengoperasian bilangan titik kambang
			int i_maks = maks(k,m,aug);
			if (aug[i_maks][k] == 0){
				continue;
			}
			tukarbaris(aug,i_maks,k,n);
			//Kurangkan baris-baris di bawah pivot
			for (int i = k + 1; i < m;i = i+1){
				double f = aug[i][k]/aug[k][k];
				//Untuk sisa elemen pada baris tersebut
				for (int j = k+1; j <= n; j = j+1)
					aug[i][j] = aug[i][j] - aug[k][j] * f;
				aug[i][k] = 0;
			}
			//TampilkanMatriks(aug,m,n);
		}

		//Ke bentuk RREF
		for (int k = m-1; k >= 0; k = k - 1){
			double f = aug[k][k];
			if (f == 0)
				continue;
			for (int j = k; j <= n; j = j + 1)
				aug[k][j] /= f;


			for (int i = k - 1; i >= 0; i = i - 1){
				f = aug[i][k]/aug[k][k];
				for (int j = n; j >= 0; j = j - 1){
				if (j != k)
					aug[i][j] -= aug[k][j] * f;
				}
				aug[i][k] = 0;
			}
		}
	}

	public static boolean IsRowZero (double [][] aug, int baris, int n){
		//Mengembalikan nilai true apabila semua elemen pada baris 'baris' pada matriks aug bernilai 0
		for (int j = 0; j < n; j = j +1){
			if (aug[baris][j] != 0.00)
				return false;
		}

		return true;
	}

	public static int RowZero (double [][] aug, int m, int n){
		//Mengembalikan indeks baris yang elemen-elemennya bernilai 0
		int row = -1;
		boolean zero;

		for (int i = 0; i < m; i = i + 1){
			zero = true;
			int j = 0;
			while (zero && j < n){
				if (aug[i][j] !=  0.00)
					zero = false;
				j += 1;
			}
			if (zero){
				row = i;
				break;
			}
		}
		return row;
	}

	public static void TampilkanMatriks(double[][] aug, int m, int n){
		//I.S : Matriks aug terdefinisi dengan dimensi m x n
		//F.S : Menampilkan matriks pada layar monitor pengguna
		System.out.println();


		System.out.println("Matriks augmented : ");
		for (int i = 0; i < m;i = i + 1){
			for (int j = 0; j < n; j = j + 1)
				System.out.printf("%.2f \t", aug[i][j]);
		System.out.printf("| %.2f%n", aug[i][n]);
		}

		System.out.println();
	}

	public static void Solusi (double [][] aug, double [] x,int m, int n){
		//I.S : Matriks aug terdefinisi dengan dimensi m x n dan array x terdefinisi dengan dimensi n
		//F.S : array x terisi dengan solusi
		for (int i = 0; i < m; i = i + 1)
			x[i] = aug[i][n];

		for (int i = 0; i < m; i = i + 1)
			System.out.printf("x%d = %.2f%n",i,x[i]);

	}

	public static void Parameter(double [][] aug, int m, int n){
		//I.S : Matriks aug terdefinisi dengan dimensi m x n
		//F.S : Menampilkan solusi dalam bentuk parameter (untuk SPL dengan banyak solusi)
		int x = 80;
		char [] para = new char [n+1];
		boolean [] z = new boolean [n+1];

		for (int j = 1; j < n; j = j + 1){
			if (aug[0][j] != 0){
				z[j] = true;
				para[j] = (char) x;
				x += 1;
				System.out.println("Misalkan x" + j + " = " + para[j]);
			}
		}

		for (int i = 0; i < m; i = i + 1){
			if (!IsRowZero(aug,i,n)){
				int left = leftmost(aug,i);
				System.out.printf("x%d = %.2f",left,aug[i][n]);
				for (int k = left + 1; k < n;k = k + 1){
					if (z[k] == true){
						if(aug[i][k] > 0)
							System.out.printf(" - %.2f%c",aug[i][k],para[k]);
						else if(aug[i][k] < 0)
							System.out.printf(" + %.2f%c",Math.abs(aug[i][k]),para[k]);
					}
				}
			}
			System.out.println();
		}
	}

	public static int leftmost (double [][] aug, int i){ 
		//Mengembalikan indeks kolom pertama pada suatu baris i yang bernilai tidak sama dengan 0
		//Prekondisi: baris tidak kosong
		int x = 0;
		while (aug[i][x] == 0)
			x += 1;

		return x;
	}

	public static void reducelagi (double [][] aug, int m, int n){
		//I.S : Matriks aug terdefinisi dengan dimensi m x n
		//F.S : Matriks aug yang lebih sederhana
		//Prekondisi: SPL memiliki solusi banyak
		for (int i = m-1; i >= 0;i = i - 1){
			if (!IsRowZero(aug,i,n)){
				int left = leftmost(aug,i);
				if (!IsLeadingOne(aug,left,i,m)){					
					double f = aug[0][left];
					for (int j = 0; j <= n;j = j + 1)
						aug[0][j] -= f*aug[i][j];
				}
			}
		}
	}

	public static void TentukanSolusi (double [][] aug,double [] x,int m, int n) throws Exception {
		//I.S : Matriks aug terdefinisi dengan dimensi m x n dan array x dengan kapasitas n terdefinisi
		//F.S : Menampilkan solusi dari SPL apabila memiliki solusi, 
		//		menampilkan pesan 'tidak memiliki solusi' jika tidak memiliki solusi
		if (RowZero(aug,m,n) != -1){
			tukarbaris(aug,RowZero(aug,m,n),m-1,n);
			reducelagi(aug,m,n);
			TampilkanMatriks(aug,m,n);
				//Jika terdapat baris yang setiap elemennya berisi 0
				if (aug[RowZero(aug,m,n)][n] != 0)
					System.out.println("Sistem persamaan linier tersebut tidak memiliki solusi.");
				else{
					System.out.println("Sistem persamaan linier tersebut memiliki banyak solusi.");
					Parameter(aug,m,n);
				}
			}
			else  {
				Solusi(aug,x,m,n);
				SimpankeFile(aug,x,m,n);
			}
	}

	public static void SimpankeFile (double [][] aug, double [] x, int m, int n) throws Exception{
		//I.S : Matriks aug terdefinisi dengan dimensi m x n dan array x dengan kapasitas n terdefinisi
		//F.S : Solusi dari SPL tersimpan ke file eksternal
		System.out.println("Simpan ke file eksternal?");
		System.out.println("1. Ya");
		System.out.println("2. Tidak");
		Scanner sc = new Scanner(System.in);
		int pil = sc.nextInt();

		if (pil == 1){
			try {
				FileWriter writer = new FileWriter("solusi.txt", true);
				for (int i = 0; i < m; i = i + 1){
					for (int j = 0; j <= n; j = j + 1){
						String cc = String.format ("%.2f", aug[i][j]);
						writer.write(cc);
						writer.write("\t");
					}
					String cc = String.format ("%n");
					writer.write(cc);
				}

				for (int i = 0; i < m; i = i + 1){
					writer.write("x");
					writer.write(Integer.toString(i));
					writer.write(" : ");
					String cc = String.format ("%.2f", x[i]);
					writer.write(cc);
					cc = String.format ("%n");
					writer.write(cc);
				}
				writer.close();
			} catch (FileNotFoundException ex){
		       	return;		     
		    }
		}
	}

	public static boolean IsLeadingOne(double [][] aug, int j, int baris, int m){
		//Menentukan apakah aug pada baris 'baris' dan kolom j merupakan satu-satunya yang bernilai tidak 0
		for (int i = baris - 1; i >= 0; i = i -1 ){
			if (aug[i][j] != 0)
				return false;
		}
		return true;
	}

	//METHOD UTAMA
	public static void main(String[] argh) throws Exception {
		Scanner sc = new Scanner(System.in);


		TampilkanMenu();

		//Memilih metode input
		int pil = sc.nextInt(); //Pilihan menu
		if (pil == 1){ //Masukan dari papan ketik
			System.out.println("Membaca masukan dari keyboard . . .");
			System.out.print("Banyaknya persamaan linier : ");
			int m = sc.nextInt();
			System.out.print("Banyaknya variabel : ");
			int n = sc.nextInt();

			double [][]aug = new double[m][n+1];
			//Pembacaan augmented matriks
			System.out.println("Masukkan matriks augmented : ");
			for (int i = 0; i < m; i = i+1){
				for (int j = 0; j <= n; j = j+1){
					aug[i][j] = sc.nextDouble();
				}
			}

			//Menampilkan augmented matriks
			TampilkanMatriks(aug,m,n);
			//Proses Eliminasi Gauss (mengubah ke bentuk row echelon)
			gaussRREF(aug,m,n);
			System.out.println("Bentuk reduced row echelon : ");
			TampilkanMatriks(aug,m,n);
			double [] x = new double [n]; //Solusi
			//Tentukan apakah SPL tersebut memiliki solusi atau tidak
			//Jika, ada tampilkan solusi
			TentukanSolusi(aug,x,m,n);
		}

		if (pil == 2){ //Masukan dari file eksternal
				System.out.println("Membaca masukan dari file eksternal . . .");
			File file=new File("matriks.txt");
	                System.out.println(file.exists());
	        try {      
	        	Scanner scan=new Scanner(file);	
	        	int m = scan.nextInt();
	        	int n = scan.nextInt();
	        	double [] [] aug = new double [m][n+1];
	        	for (int i = 0; i < m; i = i + 1)
	        		for (int j = 0; j <= n; j = j + 1)
	        			aug[i][j] = scan.nextDouble();
	        //Menampilkan augmented matriks
			TampilkanMatriks(aug,m,n);
			//Proses Eliminasi Gauss (mengubah ke bentuk row echelon)
			gaussRREF(aug,m,n);
			System.out.println("Bentuk reduced row echelon : ");
			TampilkanMatriks(aug,m,n);
			double [] x = new double [n]; //Solusi
			//Tentukan apakah SPL tersebut memiliki solusi atau tidak
			//Jika ada, tampilkan solusi
			TentukanSolusi(aug,x,m,n);

			scan.close();
	        } catch (FileNotFoundException ex){
	        	return;
	        }
		}

		if (pil == 3){
			Scanner scan = new Scanner(System.in);
			System.out.println("Masukkan n : ");

			int n = scan.nextInt();
			double[][] aug = new double[n][n+1];
			for (int i = 0; i < n; i = i + 1){
				for (int j = 0; j < n; j = j + 1)
					aug[i][j] = (float) 1/(float) (i+j+1);
				aug[i][n] = 1; 
			}
			//Menampilkan augmented matriks
			TampilkanMatriks(aug,n,n);
			//Proses Eliminasi Gauss (mengubah ke bentuk row echelon)
			gaussRREF(aug,n,n);
			System.out.println("Bentuk reduced row echelon : ");
			TampilkanMatriks(aug,n,n);
			double [] x = new double [n]; //Solusi
			//Tentukan apakah SPL tersebut memiliki solusi atau tidak
			//Jika ada, tampilkan solusi
			TentukanSolusi(aug,x,n,n);
			scan.close();
			}


		if (pil == 4){
			Scanner scan = new Scanner(System.in);
			
			System.out.println("Masukkan selang bawah: ");
			int a = scan.nextInt(); //selang

			System.out.println("Masukkan selang atas: ");
			int b = scan.nextInt(); //selang

			System.out.println("Masukkan n : ");
			int n = scan.nextInt();
			double h = (float) (b-a) / (float) n;
			double[][] aug = new double[n+1][n+2];

			for (int i = 0; i <= n; i = i + 1){
				for (int j = 0; j <= n; j = j + 1)
					aug[i][j] = Math.pow ((a + (i*h)), j);
				aug[i][n+1] = Math.pow (Math.E, -aug[i][1]) / (1 + Math.sqrt(aug[i][1]) + Math.pow(aug[i][1],2)); 
			}
			//Menampilkan augmented matriks
			TampilkanMatriks(aug,n+1,n+1);
			//Proses Eliminasi Gauss (mengubah ke bentuk row echelon)
			gaussRREF(aug,n+1,n+1);
			System.out.println("Bentuk reduced row echelon : ");
			TampilkanMatriks(aug,n+1,n+1);
			double [] x = new double [n+1]; //Solusi
			//Tentukan apakah SPL tersebut memiliki solusi atau tidak
			//Jika ada, tampilkan solusi
			TentukanSolusi(aug,x,n+1,n+1);
			System.out.printf("f(x) = %.2f",x[0]);
			for (int i = 1; i <= n; i = i + 1){
				if (x[i] > 0.00)
					System.out.printf(" + %.2fx^%d",x[i],i);
				if (x[i] < -0.00)
					System.out.printf(" - %.2fx^%d",Math.abs(x[i]),i);
			}
			scan.close();
			}

			if (pil == 5){
			Scanner scan = new Scanner(System.in);
			
			double[][] aug = new double[7][8];

			int n = 7;

			System.out.println("Masukkan nilai2 x: ");
			for (int i = 0; i < n; i = i + 1) {
				aug[i][1] = scan.nextDouble();
			}

			System.out.println("Masukkan nilai2 f(x): ");
			for (int i = 0; i < n; i = i + 1){
				for (int j = 0; j <= n; j = j + 1) {
					aug[i][j] = Math.pow (aug[i][1], j);
				}
				aug[i][7] = scan.nextDouble();
			}
			//Menampilkan augmented matriks
			TampilkanMatriks(aug,n,n);
			//Proses Eliminasi Gauss (mengubah ke bentuk row echelon)
			gaussRREF(aug,n,n);
			System.out.println("Bentuk reduced row echelon : ");
			TampilkanMatriks(aug,n,n);
			double [] x = new double [n]; //Solusi
			//Tentukan apakah SPL tersebut memiliki solusi atau tidak
			//Jika ada, tampilkan solusi
			TentukanSolusi(aug,x,n,n);
			System.out.printf("f(x) = %.2f",x[0]);
			for (int i = 1; i < n; i = i + 1){
				if (x[i] > 0.00)
					System.out.printf(" + %.2fx^%d",x[i],i);
				if (x[i] < -0.00)
					System.out.printf(" - %.2fx^%d",Math.abs(x[i]),i);
			}
			System.out.println();
			System.out.println("Masukkan nilai x yang diinginkan: ");
			double xin = scan.nextDouble();
			double hasil = 0;
			for (int i = 0; i < n; i = i + 1) {
				hasil = hasil + x[i]*Math.pow (xin, i);
			}
			System.out.printf("%.2f",hasil);

			scan.close();
			}
		
			if (pil == 6){
			Scanner scan = new Scanner(System.in);
			
			double[][] aug = new double[8][9];

			int n = 8;

			System.out.println("Masukkan pasangan tahun dengan harga rumah: ");
			for (int i = 0; i < n; i = i + 1){
				aug[i][1] = scan.nextDouble();
				for (int j = 0; j <= n; j = j + 1) {
					aug[i][j] = Math.pow (aug[i][1], j);
				}
				aug[i][8] = scan.nextDouble();
			}
			//Menampilkan augmented matriks
			TampilkanMatriks(aug,n,n);
			//Proses Eliminasi Gauss (mengubah ke bentuk row echelon)
			gaussRREF(aug,n,n);
			System.out.println("Bentuk reduced row echelon : ");
			TampilkanMatriks(aug,n,n);
			double [] x = new double [n]; //Solusi
			//Tentukan apakah SPL tersebut memiliki solusi atau tidak
			//Jika ada, tampilkan solusi
			TentukanSolusi(aug,x,n,n);
			System.out.printf("f(x) = %.2f",x[0]);
			for (int i = 1; i < n; i = i + 1){
				if (x[i] > 0.00)
					System.out.printf(" + %.2fx^%d",x[i],i);
				if (x[i] < -0.00)
					System.out.printf(" - %.2fx^%d",Math.abs(x[i]),i);
			}
			System.out.println();
			System.out.println("Masukkan tahun tertentu: ");
			double xin = scan.nextDouble();
			double hasil = 0;
			for (int i = 0; i < n; i = i + 1) {
				hasil = hasil + x[i]*Math.pow (xin, i);
			}
			System.out.printf("Prediksi harga rumah baru: %.2f",hasil);

			scan.close();
			}

			if (pil == 7){
			Scanner scan = new Scanner(System.in);
			
			double[][] aug = new double[7][8];

			int n = 6;

			System.out.println("Masukkan nilai2 Suhu (T) dalam Farenheit: ");
			for (int i = 0; i < n; i = i + 1) {
				aug[i][1] = scan.nextDouble();
			}

			System.out.println("Masukkan nilai2 viskositas kinematika air (v): ");
			for (int i = 0; i < n; i = i + 1){
				for (int j = 0; j < n; j = j + 1) {
					aug[i][j] = Math.pow (aug[i][1], j);
				}
				aug[i][6] = scan.nextDouble();
			}
			//Menampilkan augmented matriks
			TampilkanMatriks(aug,n,n);
			//Proses Eliminasi Gauss (mengubah ke bentuk row echelon)
			gaussRREF(aug,n,n);
			System.out.println("Bentuk reduced row echelon : ");
			TampilkanMatriks(aug,n,n);
			double [] x = new double [n]; //Solusi
			//Tentukan apakah SPL tersebut memiliki solusi atau tidak
			//Jika ada, tampilkan solusi
			TentukanSolusi(aug,x,n,n);
			System.out.printf("f(x) = %.2f",x[0]);
			for (int i = 1; i < n; i = i + 1){
				if (x[i] > 0.00)
					System.out.printf(" + %.2fx^%d",x[i],i);
				if (x[i] < -0.00)
					System.out.printf(" - %.2fx^%d",Math.abs(x[i]),i);
			}
			System.out.println();
			System.out.println("Masukkan nilai suhu tertentu: ");
			double xin = scan.nextDouble();
			double hasil = 0;
			for (int i = 0; i < n; i = i + 1) {
				hasil = hasil + x[i]*Math.pow (xin, i);
			}
			System.out.printf("Taksiran Viskositas: %.2f",hasil);

			scan.close();
			}
		}

		

	}
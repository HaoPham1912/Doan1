package demo;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		double[] toado = new double[] { 0, 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3, 3.25, 3.5, 3.75,
				4, 4.25, 4.5, 4.75, 5 };

		// A/Khởi tạo point support 2 chức năng:
		Scanner scan = new Scanner(System.in);
		inputMethodSelectionDescription();
		int method = methodSelection(scan);
		Point[] pointList;
		if (method == 1) {
			pointList = inputRandom(scan, toado);
		} else {
			pointList = inputManual(scan, toado);
		}

		System.out.println("Danh sach diem:");
		// B/ Tạo danh sách cluster
		Cluster[] clusterList = new Cluster[pointList.length];
		for (int i = 0; i < pointList.length; i++) {
			Point p = pointList[i];
			Cluster c = new Cluster();
			c.setName(p.getName());
			c.addPoint(p);
			clusterList[i] = c;
			System.out.println(p.toString());
		}

		// C/ Tao ma trân khoảng cách giua cac diem
		// Map<String, Double> matranKCPoint = new HashMap<>();
		// for (int i = 0; i < pointList.length - 1; i++) {
		// Point p1 = pointList[i];
		// for (int j = i + 1; j < pointList.length; j++) {
		// Point p2 = pointList[j];
		// String key = p1.getName() + "-" + p2.getName(); // vi du A-B
		// double kc = tinhkhoangcach(p1, p2);
		// matranKCPoint.put(key, kc);
		// }
		// }
		KhoangCach[] khoangCachPoint = new KhoangCach[0];
		for (int i = 0; i < pointList.length - 1; i++) {
			Point p1 = pointList[i];
			for (int j = i + 1; j < pointList.length; j++) {
				Point p2 = pointList[j];
				String key = p1.getName() + "-" + p2.getName(); // vi du A-B
				double kc = tinhkhoangcach(p1, p2);
				KhoangCach khoangCach = new KhoangCach();
				khoangCach.setName(key);
				khoangCach.setKc(kc);
				// add vo array
				khoangCachPoint = KhoangCach.appendKhoangCachToArray(khoangCachPoint, khoangCach);
			}
		}

		// D/ Tinh khoảng cách giữa các cluster
		// Map<String, Double> matranKCCluster = new HashMap<>();
		// for (int i = 0; i < clusterList.length - 1; i++) {
		// Cluster c1 = clusterList[i];
		// for (int j = i + 1; j < clusterList.length; j++) {
		// Cluster c2 = clusterList[j];
		// String key = c1.getName() + "-" + c2.getName(); // vi du A-B
		// double kc = tinhkhoangcachcluster(matranKCPoint, c1, c2);
		// matranKCCluster.put(key,kc);
		// }
		// }
		KhoangCach[] khoangCachCluster = new KhoangCach[0];
		for (int i = 0; i < clusterList.length - 1; i++) {
			Cluster c1 = clusterList[i];
			for (int j = i + 1; j < clusterList.length; j++) {
				Cluster c2 = clusterList[j];
				String name = c1.getName() + "-" + c2.getName();
				KhoangCach kcPointMin = tinhkhoangcachcluster(khoangCachPoint, c1, c2);
				KhoangCach kcCluster = new KhoangCach();
				kcCluster.setName(name);
				kcCluster.setKc(kcPointMin.getKc());
				// add vo array
				khoangCachCluster = KhoangCach.appendKhoangCachToArray(khoangCachCluster, kcCluster);
			}
		}

		// tuday se loop cho den khi clusterList chi con 1 phan tu
		while (clusterList.length > 1) {
			System.out.println("------------Data------------");
			// in data
			for (int i = 0; i < clusterList.length; i++) {
				System.out.println(clusterList[i]);
			}
			for (int i = 0; i < khoangCachCluster.length; i++) {
				System.out.println(khoangCachCluster[i]);
			}
			System.out.println("----------------------------");

			KhoangCach khoangcachnhonhat2cluster = timkhaongcachnhonhat(khoangCachCluster);
			String name = khoangcachnhonhat2cluster.getName(); // vi du A-B
			// Remove khoang cach ra khoi ma tran
			khoangCachCluster = removekhoangcachbyname(khoangCachCluster, name);
			// lay ten 2 cluster tu name ra
			String[] nameArr = name.split("-");
			String cluster1Name = nameArr[0];
			String cluster2Name = nameArr[1];
			Cluster c1 = layclusternyname(clusterList, cluster1Name);
			Cluster c2 = layclusternyname(clusterList, cluster2Name);
			// xoa 2 cluster da lay
			clusterList = removeclusterbyname(clusterList, cluster1Name);
			clusterList = removeclusterbyname(clusterList, cluster2Name);
			// Xoa khoang cach tu 2 cluster da lay den cac cluster khac
			khoangCachCluster = removekhoangcachbyclustername(khoangCachCluster, cluster1Name);
			khoangCachCluster = removekhoangcachbyclustername(khoangCachCluster, cluster2Name);
			// gop 2 cluster
			Cluster clustermoi = gopcluster(c1, c2);
			// Tinh lai khoang cach cua cluster moi toi cac cluster con lai truoc khi add no
			// vo array cluster
			for (int i = 0; i < clusterList.length; i++) {
				Cluster c = clusterList[i];
				String kcname = clustermoi.getName() + "-" + c.getName();
				KhoangCach kcPointMin = tinhkhoangcachcluster(khoangCachPoint, clustermoi, c);
				KhoangCach kcCluster = new KhoangCach();
				kcCluster.setName(kcname);
				kcCluster.setKc(kcPointMin.getKc());
				// add vo array
				khoangCachCluster = KhoangCach.appendKhoangCachToArray(khoangCachCluster, kcCluster);
			}
			// Them cluster moi vao trong danh sach cluster clusterList
			clusterList = Cluster.appendClusterToArray(clusterList, clustermoi);
		}
	}

	// xoa khoang cach tu cluster da chon den tat cac cac cluster khac
	public static KhoangCach[] removekhoangcachbyclustername(KhoangCach[] khoangCachCluster, String clusterName) {
		KhoangCach[] arr = new KhoangCach[0];
		for (int i = 0; i < khoangCachCluster.length; i++) {
			KhoangCach tmp = khoangCachCluster[i];
			String kcName = tmp.getName();
			// neu ten khoang cach bat dau bang clusternam + "-" hoac ket thuc bang "-" +
			// clusterName se bi xoa
			// vi du clustetName=A thi se xoa cac khoang cach bat dau bang A- hoac ket thuc
			// bang -A
			if (kcName.startsWith(clusterName + "-") || kcName.endsWith("-" + clusterName)) {
				// k add vo arr
			} else {
				arr = KhoangCach.appendKhoangCachToArray(arr, tmp);
			}
		}
		return arr;
	}

	public static KhoangCach[] removekhoangcachbyname(KhoangCach[] khoangCachCluster, String name) {
		int n = khoangCachCluster.length;
		if (n <= 1) {
			return new KhoangCach[0];
		}
		KhoangCach[] tmp = new KhoangCach[n - 1];
		boolean daxoa = false; // sau khi xoa index se thay doi
		for (int i = 0; i < khoangCachCluster.length; i++) {
			KhoangCach kc = khoangCachCluster[i];
			if (kc.getName().equals(name)) {
				daxoa = true;
			} else {
				if (daxoa == true) {
					tmp[i - 1] = khoangCachCluster[i]; // index se bi giam
				} else {
					tmp[i] = khoangCachCluster[i];
				}
			}
		}
		return tmp;
	}

	public static Cluster gopcluster(Cluster c1, Cluster c2) {
		System.out.println("Gop cluster " + c1.getName() + " va cluster " + c2.getName());
		Cluster cnew = new Cluster();
		// ten moi bang ten 2 cluster cong vs nhau vi du AB
		String newname = c1.getName() + c2.getName();
		cnew.setName(newname);
		// set point cua c1 cho cluster moi
		cnew.setPointList(c1.getPointList());
		// add point cua c2 cho cluster moi
		for (int i = 0; i < c2.getPointList().length; i++) {
			cnew.addPoint(c2.getPointList()[i]);
		}
		return cnew;
	}

	public static Cluster[] removeclusterbyname(Cluster[] clusterList, String name) {
		int n = clusterList.length;
		if (n <= 1) {
			return new Cluster[0];
		}
		Cluster[] tmp = new Cluster[n - 1];
		boolean daxoa = false; // sau khi xoa index se thay doi
		for (int i = 0; i < clusterList.length; i++) {
			Cluster c = clusterList[i];
			if (c.getName().equals(name)) {
				daxoa = true;
			} else {
				if (daxoa == true) {
					tmp[i - 1] = clusterList[i]; // index se bi giam
				} else {
					tmp[i] = clusterList[i];
				}
			}
		}
		return tmp;
	}

	public static Cluster layclusternyname(Cluster[] clusterList, String name) {
		for (int i = 0; i < clusterList.length; i++) {
			Cluster c = clusterList[i];
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	public static void inputMethodSelectionDescription() {
		System.out.println("Chon mot trong 2 cach input data ben duoi:");
		System.out.println("1. Random");
		System.out.println("2. Manual");
		System.out.print("Nhap input method: ");
	}

	public static int methodSelection(Scanner scan) {
		int method = -1;
		while (method == -1) {
			try {
				method = scan.nextInt();
				if (method < 1 || method > 2) {
					method = -1;
					System.out.print("Method khong hop le. Nhap input method: ");
				} else {
					System.out.println("Ban da chon method:" + method);
				}
			} catch (Exception e) {
				System.out.print("Method khong hop le. Nhap input method: ");
			}
		}
		return method;
	}

	public static Point[] inputRandom(Scanner scan, double[] toado) {
		System.out.println("Khi chon cach input random, toa do X,Y se duoc chon ngau nhien tu danh sach sau:");
		System.out.println(
				"0, 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3, 3.25, 3.5, 3.75, 4, 4.25, 4.5, 4.75, 5");
		System.out.print("Nhap so diem (toi da 26):");
		int pointNumber = -1;
		while (pointNumber == -1) {
			try {
				pointNumber = scan.nextInt();
				if (pointNumber < 1 || pointNumber > 26) {
					pointNumber = -1;
					System.out.print("Gia tri khong hop le. Nhap so diem (toi da 26):");
				} else {
					System.out.println("So diem da nhap:" + pointNumber);
				}
			} catch (Exception e) {
				System.out.print("Gia tri khong hop le. Nhap so diem (toi da 26):");
			}
		}
		System.out.println("Ten cua point se duoc lay theo thu tu bang chu cai.");
		String[] nameList = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
				"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		Random d = new Random();
		int posX = 0;
		int posY = 0;
		Point[] pointList = new Point[pointNumber];
		for (int i = 0; i < pointNumber; i++) {
			posX = d.nextInt(21);
			posY = d.nextInt(21);
			while (checkExist(pointList, toado[posX], toado[posY])) {
				posX = d.nextInt(21);
				posY = d.nextInt(21);
			}
			Point p = new Point();
			p.setName(nameList[i]);
			p.setX(toado[posX]);
			p.setY(toado[posY]);
			pointList[i] = p;
		}
		return pointList;
	}

	public static Point[] inputManual(Scanner scan, double[] toado) {
		System.out.println("Khi chon cach input manual, toa do X,Y chi duoc con tu cac gia tri sau:");
		System.out.println(
				"0, 0.25, 0.5, 0.75, 1, 1.25, 1.5, 1.75, 2, 2.25, 2.5, 2.75, 3, 3.25, 3.5, 3.75, 4, 4.25, 4.5, 4.75, 5");
		System.out.print("Nhap so diem (toi da 26):");
		int pointNumber = -1;
		while (pointNumber == -1) {
			try {
				pointNumber = scan.nextInt();
				if (pointNumber < 1 || pointNumber > 26) {
					pointNumber = -1;
					System.out.print("Gia tri khong hop le. Nhap so diem (toi da 26):");
				} else {
					System.out.println("So diem da nhap:" + pointNumber);
				}
			} catch (Exception e) {
				System.out.print("Gia tri khong hop le. Nhap so diem (toi da 26):");
			}
		}
		System.out.println("Ten cua point se duoc lay theo thu tu bang chu cai.");
		String[] nameList = new String[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
				"P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z" };
		Point[] pointList = new Point[pointNumber];
		for (int i = 0; i < pointNumber; i++) {
			System.out.println("Nhap toa do diem " + i + 1);
			System.out.print("Nhap X:");
			double x = scan.nextDouble();
			while (!checkToadoValid(toado, x)) {
				System.out.print("Nhap X:");
				x = scan.nextDouble();
			}
			System.out.print("Nhap Y:");
			double y = scan.nextDouble();
			while (!checkToadoValid(toado, y)) {
				System.out.print("Nhap Y:");
				y = scan.nextDouble();
			}
			Point p = new Point();
			p.setName(nameList[i]);
			p.setX(x);
			p.setY(y);
			pointList[i] = p;
		}
		return pointList;
	}

	public static boolean checkToadoValid(double[] toado, double input) {
		for (int i = 0; i < toado.length; i++) {
			if (toado[i] == input) {
				return false;
			}
		}
		return true;
	}

	public static boolean checkExist(Point[] pointList, double x, double y) {
		for (int i = 0; i < pointList.length; i++) {
			Point p = pointList[i];
			if (p != null && p.getX() == x && p.getY() == y) {
				return true;
			}
		}
		return false;
	}

	// Add phan tu vo array
	// public static Object[] append(Object[] arr, Object element) {
	// final int N = arr.length;
	// Object[] tmpArr = new Object[N + 1];
	// for (int i = 0; i < arr.length; i++) {
	// tmpArr[i] = arr[i];
	// }
	// tmpArr[N] = element;
	// return tmpArr;
	// }

	// tinh khoang cach giua 2 diem lam tron 2 chu so
	public static double tinhkhoangcach(Point a, Point b) {
		double kc = Math.sqrt(Math.pow(a.getX() - b.getX(), 2) + Math.pow(a.getY() - b.getY(), 2));
		return Math.round((kc * 100) / 100.0);
	}

	// tinh khoang cach giua 2 cluster
	// public static double tinhkhoangcachcluster(Map<String, Double> matranKCPoint,
	// Cluster a, Cluster b) {
	// Point[] clusterAPointList = a.getPointList();
	// Point[] clusterBPointList = b.getPointList();
	// double minKhoangCach = Double.MAX_VALUE;
	// String daidienClusterAPointName = "";
	// String daidienClusterBPointName = "";
	// for (int i = 0; i < clusterAPointList.length; i++) {
	// Point p1 = clusterAPointList[i];
	// for (int j = 0; j < clusterBPointList.length; j++) {
	// Point p2 = clusterAPointList[j];
	// String mapKey = p1.getName() + "-" + p2.getName();
	// double kc = matranKCPoint.get(mapKey);
	// if (kc < minKhoangCach) {
	// minKhoangCach = kc;
	// daidienClusterAPointName = p1.getName();
	// daidienClusterBPointName = p2.getName();
	// }
	// }
	// }
	// return minKhoangCach;
	// }
	public static KhoangCach tinhkhoangcachcluster(KhoangCach[] matrankc, Cluster a, Cluster b) {
		Point[] clusterAPointList = a.getPointList();
		Point[] clusterBPointList = b.getPointList();
		double minkc = Double.MAX_VALUE;
		KhoangCach minKhoangCachObj = null;
		for (int i = 0; i < clusterAPointList.length; i++) {
			Point p1 = clusterAPointList[i];
			for (int j = 0; j < clusterBPointList.length; j++) {
				Point p2 = clusterBPointList[j];
				String namexuoi = p1.getName() + "-" + p2.getName(); // vi du A-B
				KhoangCach kc = timkhoangcachbyname(matrankc, namexuoi);
				if (kc != null && kc.getKc() < minkc) {
					minkc = kc.getKc();
					minKhoangCachObj = kc;
				}
				String namenguoc = p2.getName() + "-" + p1.getName(); // vidu B-A
				KhoangCach kcnguoc = timkhoangcachbyname(matrankc, namenguoc);
				if (kcnguoc != null && kcnguoc.getKc() < minkc) {
					minkc = kcnguoc.getKc();
					minKhoangCachObj = kcnguoc;
				}
			}
		}
		return minKhoangCachObj;
	}

	public static KhoangCach timkhoangcachbyname(KhoangCach[] matrankc, String name) {
		for (int i = 0; i < matrankc.length; i++) {
			KhoangCach kc = matrankc[i];
			if (kc.getName().equals(name)) {
				return kc;
			}
		}
		return null;
	}

	public static KhoangCach timkhaongcachnhonhat(KhoangCach[] khoangCachCluster) {
		double minkc = Double.MAX_VALUE;
		KhoangCach minKhoangCachObj = null;
		for (int i = 0; i < khoangCachCluster.length; i++) {
			KhoangCach tmp = khoangCachCluster[i];
			if (tmp.getKc() < minkc) {
				minkc = tmp.getKc();
				minKhoangCachObj = tmp;
			}
		}
		return minKhoangCachObj;
	}
}

class Point {
	String name;
	double x;
	double y;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getX() {
		return x;
	}

	public void setX(double x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

	public String toString() {
		String s = "Point " + name + " with X=" + x + " and Y=" + y;
		return s;
	}
}

class Cluster {
	String name;
	Point[] pointList;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Point[] getPointList() {
		return pointList;
	}

	public void setPointList(Point[] pointList) {
		this.pointList = pointList;
	}

	public Cluster() {
		super();
		pointList = new Point[0];
	}

	// dung de add mot point to cluster
	public void addPoint(Point p) {
		int n = this.pointList.length;
		Point[] tmp = new Point[n + 1];
		for (int i = 0; i < n; i++) {
			tmp[i] = this.pointList[i];
		}
		tmp[n] = p;
		this.pointList = tmp;
	}

	// Add khoang cach to array
	public static Cluster[] appendClusterToArray(Cluster[] arr, Cluster kc) {
		int n = arr.length;
		Cluster[] tmp = new Cluster[n + 1];
		for (int i = 0; i < n; i++) {
			tmp[i] = arr[i];
		}
		tmp[n] = kc;
		return tmp;
	}

	public String toString() {
		String s = "Cluster  " + name + "  gom cac diem: ";
		for (int i = 0; i < pointList.length; i++) {
			s += pointList[i].toString() + " & ";
		}
		return s;
	}
}

class KhoangCach {
	String name;
	double kc;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getKc() {
		return kc;
	}

	public void setKc(double kc) {
		this.kc = kc;
	}

	// Add khoang cach to array
	public static KhoangCach[] appendKhoangCachToArray(KhoangCach[] arr, KhoangCach kc) {
		int n = arr.length;
		KhoangCach[] tmp = new KhoangCach[n + 1];
		for (int i = 0; i < n; i++) {
			tmp[i] = arr[i];
		}
		tmp[n] = kc;
		return tmp;
	}

	public String toString() {
		String s = "Khoang cach " + name + " gia tri: " + kc;
		return s;
	}
}
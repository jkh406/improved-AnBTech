package com.anbtech.util;
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.StringTokenizer;

public class SortArray
{
	/***************************************************************************
	 * ������ 
	 **************************************************************************/
	public SortArray()
	{
		
	}	

	//-------------------------------------------------------------------------
	 //
	 //		1���� String �迭 �����ϱ�
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : ��������
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 ****************************************************************************/
	 public int bubbleSortStringAsc(String[] b)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;
		 
		 //�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else rn = 1;

		//�����ϱ�
		for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i].compareTo(b[i+1]) > 0) {
					 swapString(b,i,i+1);
				 }
			 }
		 }

		 return rn;
	}

	/***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : ��������
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 ****************************************************************************/
	 public int bubbleSortStringDesc(String[] b)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;
		 
		 //�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else rn = 1;

		 //�����ϱ�
		for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i].compareTo(b[i+1]) < 0) {
					 swapString(b,i,i+1);
				 }
			 }
		 }
		 return rn;
	}

	/***************************************************************************
	 *  �迭�� �� ���Ҹ� �ٲ۴� : ��������
	 ****************************************************************************/
	private void swapString(String[] c, int first, int second)
	{
		String hold;

		hold = c[first];
		c[first] = c[second];
		c[second] = hold;
	}

	//-------------------------------------------------------------------------
	 //
	 //		������ String �迭 �����ϱ�
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : ��������
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 *  return -1 : Sort�� �迭��ȣ���� �迭�� ���� ������ ������
	 ****************************************************************************/
	 public int bubbleSortStringMultiAsc(String[][] b,int sort_no)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;

		//�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else if(b[0].length -1 < sort_no) { rn = -1; return rn; }
		 else rn=1;

		 for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i][sort_no].compareTo(b[i+1][sort_no]) > 0) {
					 swapStringMulti(b,i,i+1);
				 }
			 }
		 }
		 return rn;
	}

	/***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : ��������
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 *  return -1 : Sort�� �迭��ȣ���� �迭�� ���� ������ ������
	 ****************************************************************************/
	 public int bubbleSortStringMultiDesc(String[][] b,int sort_no)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;

		//�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else if(b[0].length -1 < sort_no) { rn = -1; return rn; }
		 else rn=1;

		 for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i][sort_no].compareTo(b[i+1][sort_no]) < 0) {
					 swapStringMulti(b,i,i+1);
				 }
			 }
		 }
		 return rn;
	}

	/***************************************************************************
	 *  �迭�� �� ���Ҹ� �ٲ۴� : ��������
	 ****************************************************************************/
	private void swapStringMulti(String[][] c, int first, int second)
	{
		String hold;
		int c_len = c[first].length;	//���� ����

		for(int i=0; i<c_len; i++) {
			hold = c[first][i];
			c[first][i] = c[second][i];
			c[second][i] = hold;
		}
	}


	 //-------------------------------------------------------------------------
	 //
	 //		1���� int �迭 �����ϱ�
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : ��������
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 ****************************************************************************/
	 public int bubbleSortAsc(int b[])
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;
		 
		 //�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else rn = 1;

		 //�����ϱ�
		 for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i] > b[i+1]) {
					 swap(b,i,i+1);
				 }
			 }
		 }
		 return rn;
	}

	/***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : �ø�����
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 ****************************************************************************/
	 public int bubbleSortDesc(int b[])
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;
		 
		 //�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else rn = 1;

		 //�����ϱ�
		 for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i] < b[i+1]) {
					 swap(b,i,i+1);
				 }
			 }
		 }
		 return rn;
	}

	/***************************************************************************
	 *  �迭�� �� ���Ҹ� �ٲ۴� : ��������
	 ****************************************************************************/
	private void swap(int c[], int first, int second)
	{
		int hold;

		hold = c[first];
		c[first] = c[second];
		c[second] = hold;
	}

	 //-------------------------------------------------------------------------
	 //
	 //		������ int �迭 �����ϱ�
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : ��������
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 *  return -1 : Sort�� �迭��ȣ���� �迭�� ���� ������ ������
	 ****************************************************************************/
	 public int bubbleSortMultiAsc(int b[][],int sort_no)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;

		//�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else if(b[0].length -1 < sort_no) { rn = -1; return rn; }
		 else rn=1;
		 
		 for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i][sort_no] > b[i+1][sort_no]) {
					 swapMulti(b,i,i+1);
				 }
			 }
		 }
		 return rn;
	}

	/***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� �����Ѵ�. : �ø�����
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Sorting �Ҷ�
	 *  return -1 : Sort�� �迭��ȣ���� �迭�� ���� ������ ������
	 ****************************************************************************/
	 public int bubbleSortMultiDesc(int b[][],int sort_no)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int b_min = b_len - 1;

		//�迭�˻��ϱ�
		 if(b_len == 0) return rn;
		 else if(b[0].length -1 < sort_no) { rn = -1; return rn; }
		 else rn=1;
		 
		 for(int pass=1; pass<b_len; pass++) {
			 for(int i=0; i<b_min; i++) {
				 if(b[i][sort_no] < b[i+1][sort_no]) {
					 swapMulti(b,i,i+1);
				 }
			 }
		 }
		 return rn;
	}

	/***************************************************************************
	 *  �迭�� �� ���Ҹ� �ٲ۴� : ��������
	 ****************************************************************************/
	private void swapMulti(int c[][], int first, int second)
	{
		int hold;
		int c_len = c[first].length;	//���� ����

		for(int i=0; i<c_len; i++) {
			hold = c[first][i];
			c[first][i] = c[second][i];
			c[second][i] = hold;
		}
	}

	//-------------------------------------------------------------------------
	 //
	 //		������ Unique String �迭 �����ϱ�
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� Unique�ϰ� �����Ѵ�. : ��������
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Unique Sorting �Ҷ�
	 *  return -1 : Sort�� �迭��ȣ���� �迭�� ���� ������ ������
	 ****************************************************************************/
	 public int bubbleUniqueSortStringMultiAsc(String[][] b,int sort_no)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int e_len = b[0].length;		//�迭 ������ ����

		 //�����ϱ�
		 rn = bubbleSortStringMultiAsc(b,sort_no);
		 if(rn != 1) return rn;
		 else rn = 1;

		//unique�� ���볢�� ���� �迭�� ���
		String[][] a = new String[b_len][e_len];

		int b_cnt = b_len -1;
		int q=0;				//���� �������	
		int an = 0;				//�迭�� ����
		for(int i=0; i<=b_cnt; i++) {
			//ó���� �߰������� ó���ϱ�
			if(i < b_cnt) {
				//sort_no�� ������ ���ڸ� ���
				if(b[i][sort_no].equals(b[i+1][sort_no])) {	
					q++;
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					q++;											//�ڽ��� ����
					for(int e=0; e<e_len; e++) a[an][e] = b[i][e];
					a[an][e_len-1] = Integer.toString(q);			//���ϰ���
					an++;
					q=0;
				}
			}
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				q++;											//�ڽ��� ����
				for(int e=0; e<e_len; e++) a[an][e] = b[i][e];	
				a[an][e_len-1] = Integer.toString(q);				//���ϰ���
				an++;
			} //else
		} //for

		//������ �迭�� ���Ұ����� 1���� �÷� �Ǹ������� ���İ��� ���
		for(int i=0; i<b_len; i++) for(int j=0; j<e_len; j++) b[i][j]="";			//clear
		for(int i=0; i<an; i++) for(int j=0; j<e_len; j++) b[i][j] = a[i][j];		//�ű��
			
		return rn;
	}

	/***************************************************************************
	 *  ��ǰ���ķ� �迭�� ���Ҹ� Unique�ϰ� �����Ѵ�. : �ø�����
	 *  return 0 : �迭�� ������ ������
	 *  return 1 : ���������� Unique Sorting �Ҷ�
	 *  return -1 : Sort�� �迭��ȣ���� �迭�� ���� ������ ������
	 ****************************************************************************/
	 public int bubbleUniqueSortStringMultiDesc(String[][] b,int sort_no)
	{
		 int rn = 0;					//���ϰ����� �迭�� ������ 0
		 int b_len = b.length;			//�迭�� ����
		 int e_len = b[0].length;		//�迭 ������ ����

		 //�����ϱ�
		 rn = bubbleSortStringMultiDesc(b,sort_no);
		 if(rn != 1) return rn;
		 else rn = 1;

		//unique�� ���볢�� ���� �迭�� ���
		String[][] a = new String[b_len][e_len];

		int b_cnt = b_len -1;
		int q=0;				//���� �������	
		int an = 0;				//�迭�� ����
		for(int i=0; i<=b_cnt; i++) {
			//ó���� �߰������� ó���ϱ�
			if(i < b_cnt) {
				//sort_no�� ������ ���ڸ� ���
				if(b[i][sort_no].equals(b[i+1][sort_no])) {	
					q++;
				} 
				//��ǰ���ڵ�� ��ǰ���ڵ尡 �ٸ��� : ���θ� �ۼ��Ѵ�.
				else {
					q++;											//�ڽ��� ����
					for(int e=0; e<e_len; e++) a[an][e] = b[i][e];
					a[an][e_len-1] = Integer.toString(q);			//���ϰ���
					an++;
					q=0;
				}
			}
			//������ �����ʹ� ������ �迭�� ��´�.
			else {
				q++;											//�ڽ��� ����
				for(int e=0; e<e_len; e++) a[an][e] = b[i][e];	
				a[an][e_len-1] = Integer.toString(q);				//���ϰ���
				an++;
			} //else
		} //for

		//������ �迭�� ���Ұ����� 1���� �÷� �Ǹ������� ���İ��� ���
		for(int i=0; i<b_len; i++) for(int j=0; j<e_len; j++) b[i][j]="";			//clear
		for(int i=0; i<an; i++) for(int j=0; j<e_len; j++) b[i][j] = a[i][j];		//�ű��
			
		return rn;
	}

	//TEST
	private void test()
	{
		String[][] a = {{"2","4"},{"6","1"},{"4","3"},{"10","12"}};
		//String[] a = {"7","6","5","8","9","2","1","-9"};
		System.out.println("return : "+bubbleSortStringMultiAsc(a,1));
		for(int i=0; i<a.length; i++) {
			for(int j=0; j<2; j++)	{
				System.out.print(a[i][j]+" : ");
			}
			System.out.println("");
		}
		
	}
}

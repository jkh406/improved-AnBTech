package com.anbtech.util;
import java.util.*;
import java.text.*;
import java.io.*;
import java.util.StringTokenizer;

public class SortArray
{
	/***************************************************************************
	 * 생성자 
	 **************************************************************************/
	public SortArray()
	{
		
	}	

	//-------------------------------------------------------------------------
	 //
	 //		1차원 String 배열 정렬하기
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  거품정렬로 배열의 원소를 정렬한다. : 내림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 ****************************************************************************/
	 public int bubbleSortStringAsc(String[] b)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;
		 
		 //배열검사하기
		 if(b_len == 0) return rn;
		 else rn = 1;

		//정렬하기
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
	 *  거품정렬로 배열의 원소를 정렬한다. : 내림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 ****************************************************************************/
	 public int bubbleSortStringDesc(String[] b)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;
		 
		 //배열검사하기
		 if(b_len == 0) return rn;
		 else rn = 1;

		 //정렬하기
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
	 *  배열의 두 원소를 바꾼다 : 내림차순
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
	 //		다차원 String 배열 정렬하기
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  거품정렬로 배열의 원소를 정렬한다. : 내림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 *  return -1 : Sort할 배열번호보다 배열의 열의 갯수가 작을때
	 ****************************************************************************/
	 public int bubbleSortStringMultiAsc(String[][] b,int sort_no)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;

		//배열검사하기
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
	 *  거품정렬로 배열의 원소를 정렬한다. : 내림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 *  return -1 : Sort할 배열번호보다 배열의 열의 갯수가 작을때
	 ****************************************************************************/
	 public int bubbleSortStringMultiDesc(String[][] b,int sort_no)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;

		//배열검사하기
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
	 *  배열의 두 원소를 바꾼다 : 내림차순
	 ****************************************************************************/
	private void swapStringMulti(String[][] c, int first, int second)
	{
		String hold;
		int c_len = c[first].length;	//열의 갯수

		for(int i=0; i<c_len; i++) {
			hold = c[first][i];
			c[first][i] = c[second][i];
			c[second][i] = hold;
		}
	}


	 //-------------------------------------------------------------------------
	 //
	 //		1차원 int 배열 정렬하기
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  거품정렬로 배열의 원소를 정렬한다. : 내림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 ****************************************************************************/
	 public int bubbleSortAsc(int b[])
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;
		 
		 //배열검사하기
		 if(b_len == 0) return rn;
		 else rn = 1;

		 //정렬하기
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
	 *  거품정렬로 배열의 원소를 정렬한다. : 올림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 ****************************************************************************/
	 public int bubbleSortDesc(int b[])
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;
		 
		 //배열검사하기
		 if(b_len == 0) return rn;
		 else rn = 1;

		 //정렬하기
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
	 *  배열의 두 원소를 바꾼다 : 내림차순
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
	 //		다차원 int 배열 정렬하기
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  거품정렬로 배열의 원소를 정렬한다. : 내림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 *  return -1 : Sort할 배열번호보다 배열의 열의 갯수가 작을때
	 ****************************************************************************/
	 public int bubbleSortMultiAsc(int b[][],int sort_no)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;

		//배열검사하기
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
	 *  거품정렬로 배열의 원소를 정렬한다. : 올림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Sorting 할때
	 *  return -1 : Sort할 배열번호보다 배열의 열의 갯수가 작을때
	 ****************************************************************************/
	 public int bubbleSortMultiDesc(int b[][],int sort_no)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int b_min = b_len - 1;

		//배열검사하기
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
	 *  배열의 두 원소를 바꾼다 : 내림차순
	 ****************************************************************************/
	private void swapMulti(int c[][], int first, int second)
	{
		int hold;
		int c_len = c[first].length;	//열의 갯수

		for(int i=0; i<c_len; i++) {
			hold = c[first][i];
			c[first][i] = c[second][i];
			c[second][i] = hold;
		}
	}

	//-------------------------------------------------------------------------
	 //
	 //		다차원 Unique String 배열 정렬하기
	 //
	 //-------------------------------------------------------------------------
	 /***************************************************************************
	 *  거품정렬로 배열의 원소를 Unique하게 정렬한다. : 내림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Unique Sorting 할때
	 *  return -1 : Sort할 배열번호보다 배열의 열의 갯수가 작을때
	 ****************************************************************************/
	 public int bubbleUniqueSortStringMultiAsc(String[][] b,int sort_no)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int e_len = b[0].length;		//배열 원소의 갯수

		 //정렬하기
		 rn = bubbleSortStringMultiAsc(b,sort_no);
		 if(rn != 1) return rn;
		 else rn = 1;

		//unique한 내용끼리 묶어 배열에 담기
		String[][] a = new String[b_len][e_len];

		int b_cnt = b_len -1;
		int q=0;				//동일 수량계산	
		int an = 0;				//배열의 갯수
		for(int i=0; i<=b_cnt; i++) {
			//처음과 중간데이터 처리하기
			if(i < b_cnt) {
				//sort_no와 같으면 숫자만 기록
				if(b[i][sort_no].equals(b[i+1][sort_no])) {	
					q++;
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					q++;											//자신의 갯수
					for(int e=0; e<e_len; e++) a[an][e] = b[i][e];
					a[an][e_len-1] = Integer.toString(q);			//동일갯수
					an++;
					q=0;
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				q++;											//자신의 갯수
				for(int e=0; e<e_len; e++) a[an][e] = b[i][e];	
				a[an][e_len-1] = Integer.toString(q);				//동일갯수
				an++;
			} //else
		} //for

		//원래의 배열에 원소갯수를 1개더 늘려 맨마지막에 정렬갯수 담기
		for(int i=0; i<b_len; i++) for(int j=0; j<e_len; j++) b[i][j]="";			//clear
		for(int i=0; i<an; i++) for(int j=0; j<e_len; j++) b[i][j] = a[i][j];		//옮기기
			
		return rn;
	}

	/***************************************************************************
	 *  거품정렬로 배열의 원소를 Unique하게 정렬한다. : 올림차순
	 *  return 0 : 배열의 갯수가 없을때
	 *  return 1 : 정상적으로 Unique Sorting 할때
	 *  return -1 : Sort할 배열번호보다 배열의 열의 갯수가 작을때
	 ****************************************************************************/
	 public int bubbleUniqueSortStringMultiDesc(String[][] b,int sort_no)
	{
		 int rn = 0;					//리턴값으로 배열의 갯수가 0
		 int b_len = b.length;			//배열의 갯수
		 int e_len = b[0].length;		//배열 원소의 갯수

		 //정렬하기
		 rn = bubbleSortStringMultiDesc(b,sort_no);
		 if(rn != 1) return rn;
		 else rn = 1;

		//unique한 내용끼리 묶어 배열에 담기
		String[][] a = new String[b_len][e_len];

		int b_cnt = b_len -1;
		int q=0;				//동일 수량계산	
		int an = 0;				//배열의 갯수
		for(int i=0; i<=b_cnt; i++) {
			//처음과 중간데이터 처리하기
			if(i < b_cnt) {
				//sort_no와 같으면 숫자만 기록
				if(b[i][sort_no].equals(b[i+1][sort_no])) {	
					q++;
				} 
				//모품목코드와 자품목코드가 다르면 : 전부를 작성한다.
				else {
					q++;											//자신의 갯수
					for(int e=0; e<e_len; e++) a[an][e] = b[i][e];
					a[an][e_len-1] = Integer.toString(q);			//동일갯수
					an++;
					q=0;
				}
			}
			//마지막 데이터는 무조건 배열에 담는다.
			else {
				q++;											//자신의 갯수
				for(int e=0; e<e_len; e++) a[an][e] = b[i][e];	
				a[an][e_len-1] = Integer.toString(q);				//동일갯수
				an++;
			} //else
		} //for

		//원래의 배열에 원소갯수를 1개더 늘려 맨마지막에 정렬갯수 담기
		for(int i=0; i<b_len; i++) for(int j=0; j<e_len; j++) b[i][j]="";			//clear
		for(int i=0; i<an; i++) for(int j=0; j<e_len; j++) b[i][j] = a[i][j];		//옮기기
			
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

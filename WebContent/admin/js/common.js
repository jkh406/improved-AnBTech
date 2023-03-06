//HIPRONET --> HIPRO ������ �ڿ� ä���. 
function fillCharRight( str, len )
{
    var strVal = str;

    if( len > strVal.length )
    {
        while( strVal.length < len )
        {
            strVal = strVal + " ";
        }
    }
    else if( len < strVal.length )
    {
        strVal = strVal.substring( 0, len );
    }
    else
    {
        strVal = strVal;
    }
    return strVal;
}

//������ ������	�´��� ���Ѵ�.
function isDate(strDate)
{
	if(!isDigit(strDate)) return false;
	var	year  =	0;
	var	month =	0;
	var	date  =	0;
	if(strDate.length <	8) return false;

	year   = parseInt(zeroDefect(strDate.substring(0, 4)));
	month  = parseInt(zeroDefect(strDate.substring(4, 6)));
	date   = parseInt(zeroDefect(strDate.substring(6, 8)));

	return isDateInt(year, month, date);
}
//from to��	���ǿ� �´��� �˻��Ѵ�.
function isFromTo(strFromDate, strToDate)
{
	var	fromDate = 0;
	var	toDate	 = 0;
	if(isDate(strFromDate))	fromDate = parseInt(strFromDate);
	else					return false;

	if(isDate(strToDate))	toDate = parseInt(strToDate);
	else					return false;

	if(fromDate	!= 0 &&	toDate != 0	&& fromDate	<= toDate) return true;
	else												   return false;
}
//�Է��� ���ڿ��� ��� ���鹮�ڸ� �����Ѵ�.
function trimForDigit(str)
{
	return replace(str,	" ", "");
}
//�Է��� ���ڿ��� �յ� ���鹮�ڸ� �����Ѵ�.
function trim(str)
{
	str	= ""+str;

	while(str.charAt(0)	== ' ')
	{
		str	= str.substring(1,str.length);
	}
	while((str.charAt(str.length-1)	== ' '))
	{
		str	= str.substring(0,str.length-1);
	}
	return str;
}
//�Է��� ���ڿ��� ��� ���ù��ڸ� �ٸ����ڷ� �ٲ۴�
function replace(strSource,	oldStr,	newStr)
{
	var	cnt			 = strSource.length;
	var	oldStrLength = oldStr.length;
	var	str	= "";
	var	i=0
	for( i=0 ; i <=	cnt-oldStrLength ; i++)
	{
		if(strSource.substring(i,i+oldStrLength) ==	oldStr)
		{
			str	+= newStr;
			i	+= oldStrLength-1;
		}
		else
		{
			str	+= strSource.substring(i,i+1);
		}
	}
	if(i < cnt)	str	+= strSource.substring(i,cnt);

	return str;
}
//999999999	<==	999,999,999
function getRealDigit(str)
{
	return replace(str,	',', '');
}
//999999999	==>	999,999,999
function getDisplayDigit(num)
{
	var	code = ""+num;
	var	rcode;
	var	i;
	var	dat_num;

	if(code.length == 0) return	"0";

	rcode="";

	dat_num=code.length;

	for(i=(dat_num-1) ;	i>=0 ; i--)
	{

	  if((dat_num-i-1) != 0)
		if((dat_num-i-1)%3 == 0)
		  if(code.charAt(i)	!= '-' && code.charAt(i+1) != '.' )
			rcode =	","	+ rcode;
	  rcode= code.charAt(i)	+ rcode;
	}

	return rcode;
}
//�ݿø��ؼ� �Ҽ��� n�ڸ����� ǥ��
//ex)getRound(10.128, 2) --> 10.13
function getRound(number, X){
    // rounds number to X decimal places, defaults to 2
    X = (!X ? 2 : X);
    return Math.round(number*Math.pow(10,X))/Math.pow(10,X);
}//end getRound

// ���ϴ� �ڸ��� �Ҽ��� ���
function getDisplayFloat( aString, nLen )
{
    var strDotLeft = '';
    var strDotRight = '';
    var strReturnString = '';
    
    if( aString.indexOf( '.' ) != -1 )
    {
        strDotRight = aString.substring( aString.indexOf( '.'), aString.length );
    }
    else
    {
        strDotRight = '.';
    }
    for( var i=0 ; i < nLen ; i++ )
    {
        strDotRight += '0';
    }
    if( aString.indexOf( '.' ) != -1 )
    {
        strDotLeft = aString.substring( 0, aString.indexOf( '.' ) );
    }
    else
    {
        strDotLeft  = aString;
    }
    if( strDotLeft.length == 0 )
    {
        strDotLeft = '0';
    }
    strReturnString = getDisplayDigit( strDotLeft ) + strDotRight.substring( 0, nLen + 1 );
    return strReturnString;
}
// �Ҽ��� ���ڸ����� ����(+)
function plusFloat( aFloat1, aFloat2 )
{
    var temp;
    var aFloat1 = replace( aFloat1, ".", "" );
    var aFloat2 = replace( aFloat2, ".", "" );
    temp = new String( eval( aFloat1 + aFloat2 ) );
    if( "0" == temp )
    {
        temp = "000";
    }
    return ( temp.substring( 0, temp.length - 2 ) + "." + temp.substring( temp.length - 2, temp.length ) );
}
// �Ҽ��� ���ڸ����� ���ϱ� ����
function plusFloat_1( aFloat1, aFloat2 )
{
    var temp;
    var aFloat3 = getRound(aFloat1,2) * 100;
    var aFloat4 = getRound(aFloat2,2) * 100;

//    temp = new String( eval( aFloat1 + aFloat2 ) );

    temp =  new String(eval(parseInt(getRound(aFloat3,2),0) + parseInt(getRound(aFloat4,2),0) ));
//	alert(temp);
    if( "0" == temp )
    {
        temp = "000";
    }
    return ( temp.substring( 0, temp.length - 2 ) + "." + temp.substring( temp.length - 2, temp.length ) );
}
// �Ҽ��� ���ڸ����� ����(-)
function minusFloat( aFloat1, aFloat2 )
{
    var temp;
    var aFloat1 = replace( aFloat1, ".", "" );
    var aFloat2 = replace( aFloat2, ".", "" );
    temp = new String( eval( aFloat1 - aFloat2 ) );
    if( "0" == temp )
    {
        temp = "000";
    }
    return ( temp.substring( 0, temp.length - 2 ) + "." + temp.substring( temp.length - 2, temp.length ) );
}
// �Ҽ��� ���ڸ����� ����(*)
function mulFloat( aFloat1, aFloat2 )
{
    var temp;
    var aFloat1 = replace( aFloat1, ".", "" );
    var aFloat2 = replace( aFloat2, ".", "" );
//    alert( "aFloat1" + eval( aFloat1 ) + ": aFloat2" + eval( aFloat2 ) );
//    alert( eval( aFloat1 * aFloat2 ) );
//    alert( "eval( aFloat1 ) * eval( aFloat2 ) = " + eval( aFloat1 ) * eval( aFloat2 ) );
    temp = new String( eval(aFloat1 * aFloat2 ) );
//    alert( "temp = " + temp );
    if( '0' == temp )
    {
        temp = "00000";
    }
    return ( temp.substring( 0, temp.length - 4 ) + "." + temp.substring( temp.length - 4, temp.length - 2 ) );
}
// �Ҽ��� ���ڸ����� ����(/)
function divFloat( aFloat1, aFloat2 )
{
    var temp;
    var aFloat1 = replace( aFloat1, ".", "" );
    var aFloat2 = replace( aFloat2, ".", "" );
    temp = new String( eval( aFloat1 / aFloat2 ) );
    if( '0' == temp )
    {
        temp = "00000";
    }
    return ( temp.substring( 0, temp.length - 4 ) + "." + temp.substring( temp.length - 4, temp.length - 2 ) );
}
//����1+����2+����3+....+����n=�Ѽ���
function setAddElement()
{
	if(setAddElement.arguments.length <	3) return;

	var	nTotQty	= 0;
	var	objTotQty =	setAddElement.arguments[eval(setAddElement.arguments.length-1)];

	for(var	i=0	; i	< setAddElement.arguments.length-1 ; i++)
	{
		checkDigit(setAddElement.arguments[i]);
		nTotQty	+= parseInt(zeroDefect(trimForDigit(setAddElement.arguments[i].value)));
	}
	objTotQty.value	= getDisplayDigit(''+nTotQty);

}
//����*�ܰ�=�ݾ�
function setSumElement(objQty, objAmt, objTotalAmt)
{
	var	objEventSrc	= event.srcElement;

	checkDigit(objEventSrc);

	var	qty		   = eval(objQty.value);

	var	highAmt	   = parseInt(eval(objAmt.value+'/10000'));
	var	lowAmt	   = eval(objAmt.value+'%10000');

	var	highTotAmt = highAmt * qty;
	var	lowTotAmt  = lowAmt	 * qty;

	highTotAmt	   = highTotAmt	+ parseInt(lowTotAmt/10000);
	lowTotAmt	   = ''+lowTotAmt%10000;

	var	totalAmt   = ''+highTotAmt + getLengthString(''+lowTotAmt,4);
	//Error
	if(totalAmt	>= 10000000000)
	{
		commonMsg(105);
		//Return SrcElement's defaultValue
		if(objEventSrc.defaultValue	== '')
		{
			objEventSrc.defaultValue = '1';
			objEventSrc.value		 = '1';
		}
		else
		{
			objEventSrc.value =	objEventSrc.defaultValue;
		}
		//Re Calculation
		qty		   = eval(objQty.value);

		highAmt	   = parseInt(eval(objAmt.value+'/10000'));
		lowAmt	   = eval(objAmt.value+'%10000');

		highTotAmt = highAmt * qty;
		lowTotAmt  = lowAmt	 * qty;

		highTotAmt	   = highTotAmt	+ parseInt(lowTotAmt/10000);
		lowTotAmt	   = ''+lowTotAmt%10000;

		totalAmt   = ''+highTotAmt + getLengthString(''+lowTotAmt,4);
		if(totalAmt	>= 10000000000)
		{
			if(objEventSrc == objQty)  objEventSrc = objAmt;
			else
				objEventSrc	= objQty;
			if(objEventSrc.defaultValue	== '')
			{
				objEventSrc.defaultValue = '1';
				objEventSrc.value		 = '1';
			}
			else
			{
				objEventSrc.value =	objEventSrc.defaultValue;
			}
		}
		setSumElement(objQty, objAmt, objTotalAmt);
		objEventSrc.select();
		objEventSrc.focus();
	}
	else
	{
		if(objTotalAmt)
		{
			objTotalAmt.value	= getDisplayDigit(zeroDefect(trimForDigit(totalAmt)));
		}
	}
}
//�Է��� ���ڿ��� �������� ���Ѵ�.
function isDigit(str)
{
	var	strDigit = '0123456789';
	if(parseInt(str) ==	NaN)		return false;
	if((''+parseInt(str)) == 'NaN')	return false;
	if(str.length == 0)				return false;
	if(parseInt(str) < 0)			return false;
	for(var	i=0	; i	< str.length ; i++)
	{
		if(strDigit.indexOf(str.charAt(i)) < 0)	return false;
	}
	return true;
}
//�Է��� ���ڿ��� ���� '0'�� �����Ѵ�.
function zeroDefect(str)
{
	str	= ''+str;
	var	length = str.length;
	for(var	i=0	; i	< length ; i++ )
	{
		if(str.charAt(0) ==	"0") str = str.substring(1,str.length);
		else					 break;
	}
	if(str.length == 0)	str='0';
	return str;
}
//������ ������	�´��� ���Ѵ�.
function isDateInt(year, month,	date)
{
	if (year < 1900	|| 9999	< year)							 return	false;
	else if	(month < 1 || 12 < month)						 return	false;
	else if	(date <	1 || getDayCountInt(year, month) < date) return	false;
	return true;
}
//���ڿ� �տ� '0'��	ä�� ���̸�ŭ��	���ڸ� ��ȯ�Ѵ�.
function getLengthString(str, len)
{
	var	strTemp	= '';
	for(i=0	; i	< len ;	i++) strTemp +=	'0';
	strTemp	+= str;
	return strTemp.substring(strTemp.length-len, strTemp.length);
}

//���ڸ� üũ�ϴµ� �־ �ƹ��͵� �Է����� �ʴ´ٸ� �׳� �������� �����ϰ� �� �ش�. ��� �ϳ��� ���ڶ� �ִٸ� �̰��� üũ�ϰ� �Ѵ�.
function checkDigit(obj)
{
	if(obj)
	{
		obj.value =	replace(obj.value, ',','');
		if(obj.value != '')
		{
			if(!isDigit(obj.value)){
				commonMsg(102);
				if(isDigit(obj.defaultValue)) obj.value	= obj.defaultValue;
				else						  obj.value	= '0';
				obj.select();
				obj.focus();
			}
			else{
				obj.value =	parseInt(obj.value);
			}
		}
	}
}
//���ڿ��� ���̸� üũ�Ѵ�.onBlur��	�Ǵ� onKeyPress�� ����
function checkByteLength(obj, maxByteLength)
{
	if(obj)
	{
		if(getByteLength(obj.value)	> maxByteLength)
		{
			commonMsg1(103,"@1",""+parseInt(maxByteLength/2),"@2",""+maxByteLength);
			obj.value =	getByteLengthString(obj.value, maxByteLength);
			obj.select();
			obj.focus();
		}
	}
}
//���ڿ��� ����Ʈ���̸�ŭ��	��ȯ�Ѵ�.(�������� �ѱ��ϰ�� 1	Byte�۰� ��ȯ�Ѵ�.)
function getByteLengthString(str,maxByteLength)
{
	var	len	= 0;
	var	i	= 0;
	if(getByteLength(str) <= maxByteLength)	return str;
	for(i=0	; len <	maxByteLength ;	i++, len++)
	{
		if(!str.substring(i, i+1)) break;
		if(isTwoByte(str.substring(i, i+1))) len++;
	}
	if(getByteLength(str.substring(0, i)) >	maxByteLength) return str.substring(0, i-1);
	return str.substring(0,	i);
}
//�Է��� ���ڿ��� ����Ʈ���̸� �����´�.(�ѱ� 2Byte	, ���� 1Byte)
function getByteLength(str)
{
	var	len=0;
	if(str == null)	return 0;
	for(i=0	; i	< str.length ; i++,	len++)
	{
		if(escape(str.substring(i, i+1)).length	== 6) len++;
	}
	return len;
}
//�Է��� ���ڰ�	2Byte���� ���Ѵ�.(���ڿ��� ��� ù ���ڸ�	��)
function isTwoByte(str)
{
	if(str.length == 1)
		return (escape(str).length == 6);
	else if(str.length > 1)
		if(escape(str.substring(i,i+1)).length == 6) return	true;
	return false;
}
//�Է��� ���ڰ�	�ѱ����� ���Ѵ�.(���ڿ��߰���	�ѱ�üũ ����)
function isHan(str)
{
	if(str.length == 1)
		if(escape(str).length == 6 && escape("��") <= escape(str) && escape(str) <=	escape("R"))
			return true;
	else if(str.length > 1)
		for(var	i =	0 ;	i <	str.length ; i++)
			if(escape(str.substring(i,i+1)).length == 6	&& escape("��")	<= escape(str.substring(i,i+1))	&& escape(str.substring(i,i+1))	<= escape("R"))
				return true;
	return false;
}
//�˻����ǿ� ���� ���ڸ� �����Ѵ�.
function checkSearchString(obj,	isMsg)
{
	if(obj)
	{
		if(	obj.value.indexOf("'" )	>= 0  ||
			obj.value.indexOf("%" )	>= 0  ||
			obj.value.indexOf("\"")	>= 0  ||
			obj.value.indexOf("_" )	>= 0  )
		{
			if(isMsg) commonMsg(104);
			obj.value =	replace(replace(replace(replace(obj.value,"'",""),"\"",""),"%",""),"_","");
			obj.focus();
			obj.select();
			return false;
		}
	}
}
// ���������� �˻��� ���� �Ǿ�ǰ��,	??��ü���� ���̸� üũ�Ѵ�.
function commonKeyPress(obj, iMaxByteLength)
{
	if(event.keyCode ==	13)
	{
		if(getByteLength(obj.value)	> iMaxByteLength)
		{
			obj.blur(false);
		}
		else
		{
			if(keyPress)
			{
				keyPress(obj);
			}
		}
	}
}
//���ó��ڸ� �����´�.
function getToday()
{
	var	date = new Date();
	return getLengthString(''+date.getYear(),4)	+ getLengthString(''+(date.getMonth()+1),2)	+ getLengthString(''+date.getDate(),2);
}

//����ð��� �����´�.
function getTime()
{
	var	date = new Date();
	return getLengthString(''+date.getHours(),2) + getLengthString(''+date.getMinutes(),2);
}

//�ð��� ���̱�	���� �������� ��ȯ�Ѵ�.
function getDisplayTime(str)
{
	if(str.length >= 4)	return str.substring(0,2) +	":"	+ str.substring(2,4);
	else				return getDisplayTime(getTime());
}
//�ð��� ��������������	��ȯ�Ѵ�.
function getRealTime(str)
{
	return replace(str,	':', '');
}
//�Ѵ����� ���ڸ� ��ȯ�Ѵ�.
function getPrevMonthToday()
{
	month =	parseInt(zeroDefect(getToday().substring(4,	6)))-1;

	return getDate(getToday().substring(0, 4)+getRightSubstring("00"+month,	2)+getToday().substring(6, 8));
}
//�Ѵ����� ���ڸ� ��ȯ�Ѵ�.
function getNextMonthToday()
{
	month =	parseInt(zeroDefect(getToday().substring(4,	6)))+1;

	return getDate(getToday().substring(0, 4)+getRightSubstring("00"+month,	2)+getToday().substring(6, 8));
}
//1������ ���ڸ� ��ȯ�Ѵ�.
function getNextYearToday()
{
	year = parseInt(zeroDefect(getToday().substring(0, 4)))+1;

	return getDate(getLengthString(''+year,	4)+getToday().substring(4, 8));
}
//���� ������ ���ڸ� ��ȯ�Ѵ�.
function getDateWithOffset(strDate,	offset)
{
	var	year  =	0;
	var	month =	0;
	var	date  =	0;

	year  =	parseInt(zeroDefect(strDate.substring(0, 4)));
	month =	parseInt(zeroDefect(strDate.substring(4, 6)));
	date  =	parseInt(zeroDefect(strDate.substring(6, 8)));

	date = date	+ offset;

	var	result = getDateInt(year, month, date);

	var	temp = getRightSubstring("0000"+result[0], 4)
			 + getRightSubstring("00"  +result[1], 2)
			 + getRightSubstring("00"  +result[2], 2);

	return temp;
}
//���ڸ� �����Ѵ�. ("20010132" ==> "20010201")
function getDateInt(year, month, day)
{
	if(year	< 1900)
	{
		alert(1);
		return getDateInt(year+=100, month,	day);
	}
	else if(year > 9999)
	{
		return getDateInt(year-=100, month,	day);
	}

	if(month < 1)
	{
		return getDateInt(--year, month+=12, day);
	}
	else if(month >	12)
	{
		return getDateInt(++year, month-=12, day);
	}
	var	maxDate	= getDayCountInt(year, month);

	if(day < 1)
	{
		maxDate	= getDayCountInt(year, --month);
		return getDateInt(year,	  month, day+=maxDate);
	}
	else if(day	> maxDate)
		return getDateInt(year,	++month, day-=maxDate);

	return new Array(year,month,day);
}
//���ڸ� �����Ѵ�. ("20010132" ==> "20010201")
function getDate(strDate)
{
	var	year  =	0;
	var	month =	0;
	var	date  =	1;

	year  =	parseInt(zeroDefect(strDate.substring(0, 4)));
	month =	parseInt(zeroDefect(strDate.substring(4, 6)));
	date  =	parseInt(zeroDefect(strDate.substring(6, 8)));

	var	result = getDateInt(year, month, date);

	var	temp = getLengthString(''+result[0], 4)
			 + getLengthString(''+result[1], 2)
			 + getLengthString(''+result[2], 2);

	return temp;
}
// �����ʿ��� ���� ���ڿ���	��ȯ�Ѵ�.
function getRightSubstring(str,	len)
{
	if(str.lenth < len)
		return str;
	else
		return str.substring(str.length	- len, str.length);
}
// ������ ������ ���̱�	���� �������� ��ȯ�Ѵ�.	"20001020" -> "2000.10.20"
function getDisplayDate(date)
{
	var	str	= "";

	if(date.length >= 4)
		str	+= date.substring(0, 4)+".";
	if(date.length >= 6)
		str	+= date.substring(4, 6)+".";
	if(date.length >= 8)
		str	+= date.substring(6, 8)
	return str;
}
// ������ ������ ���̱�	���� �������� ��ȯ�Ѵ�.	"20001020" -> "Oct 20, 2000"
function getDisplayDateE(date)
{
	var	strMonth = new Array("JAN","FEB","MAR","APR","MAY","JUN","JUL","AUG","SEP","OCT","NOV","DEC");
	if(isDate(date))
	{
		var	i =	parseInt(zeroDefect(date.substring(4,6))) -	1;
		date = strMonth[i]+" "+date.substring(6,8)+", "+date.substring(0, 4);
	}
	return date;
}
// ������ ������ ������	�������� ��ȯ�Ѵ�. "2000.10.20"	-> "20001020"
function getRealDate(date)
{
	return replace(date, ".", "");
}
// �̹�����	���� ���� ��ȯ�Ѵ�.
function getDayCountInt(year, month)
{
	var	days = new Array(31,28,31,30,31,30,31,31,30,31,30,31);

	year  += parseInt((month-1)/12);
	month  = (month%12 == 0) ? 12 :	month%12;

	if(year	 < 1900)			   return getDayCountInt(year+=100,	month);
	else if(year  >	9999)		   return getDayCountInt(year-=100,	month);

	if(month ==	2)
	{
		if(year%400	== 0)	   return 29;
		else if(year%100 ==	0) return days[month-1];
		else if(year%4 == 0)   return 29;
	}
	return days[month-1];
}
// �̹�����	���� ���� ��ȯ�Ѵ�.
function getDayCount(strDate)
{
	var	year  =	parseInt(zeroDefect(strDate.substring(0, 4)));
	var	month =	parseInt(zeroDefect(strDate.substring(4, 6)));

	return getDayCountInt(year,	month);
}
// ������ ��ȯ�Ѵ�.
function getDay(strDate)
{
	var	year   = 1900;
	var	month  = 0;
	var	date   = 1;
	var	day	   = 0;

	strDate	= getDate(strDate);

	year  =	parseInt(zeroDefect(strDate.substring(0, 4)));
	month =	parseInt(zeroDefect(strDate.substring(4, 6)))-1;
	date  =	parseInt(zeroDefect(strDate.substring(6, 8)));

	var	tmp	= getDateInt(year,month,date);

	var	tmp1 = new Date(tmp[0],tmp[1],tmp[2]);

	return tmp1.getDay();
}
//�ð��� ������	�ùٸ��� ���Ѵ�.
function checkTime(obj)
{
	if(!isTime(obj.value) || obj.value.length != 4)
	{
		commonMsg(108);
		if(isTime(obj.defaultValue))
			obj.value =	obj.defaultValue;
		else
			obj.value =	getTime();
		obj.focus();
	}
}
//������ ������	�´��� ���Ѵ�.
function isTime(strTime)
{
	if(!isDigit(strTime))  return false;

	var	hour   = 0;
	var	minute = 0;
	if(strTime.length <	4) return false;

	hour	= parseInt(zeroDefect(strTime.substring(0, 2)));
	minute	= parseInt(zeroDefect(strTime.substring(2, 4)));

	if(	0 <= hour &&  hour < 24	&& 0 <=	minute && minute < 60)
		return true;
	else
		return false;
}
//������ ������	üũ�ϰ� Ʋ����	�޽����� ����	��Ŀ���� �̵��Ѵ�.
function checkDate(obj)
{
	if(!isDate(obj.value) || obj.value.length != 8)
	{
		commonMsg(100);
		if(isDate(obj.defaultValue))
			obj.value =	obj.defaultValue;
		else
			obj.value =	getToday();
		obj.focus();
	}
}
// YYYYMM��	üũ�Ѵ�.
function checkYYYYMM(obj)
{
	var	strDate	= obj.value	+ '01';
	if(!isDate(strDate))
	{
		commonMsg(106);
		strDate	= obj.defaultValue+'01';

		if(isDate(strDate))
			obj.value =	obj.defaultValue;
		else
			obj.value =	getToday().substring(0,6);
		obj.focus();
	}
}
//����(float��)�� ������ üũ�ϰ� Ʋ���� �޽����� ���� ��Ŀ���� �̵��Ѵ�.
function checkFloat(obj, nLen)
{
	if(obj)
	{
		obj.value =	replace(obj.value, ',',	'');
		if(!isFloat(obj.value))
		{
			commonMsg(102);
			obj.value =	getFloat(obj.value,	nLen);
			obj.focus();
			obj.select();
		}
		else
		{
			obj.value =	getFloat(obj.value,	nLen);
		}
	}
}
//����(float��)�� ������ üũ�Ͽ� Ʋ���� �����Ѵ�.
function getFloat(str, nLen)
{
	str	= ""+str;
	var	strDotRight	= "";
	var	strDotLeft	= "";

	if(str.indexOf(".")	!= -1)	  strDotLeft  =	str.substring(0, str.indexOf("."));
	else						  strDotLeft  =	str;

	if(str.indexOf(".")	!= -1)	  strDotRight =	str.substring((str.indexOf(".")+1),	str.length);
	else						  strDotRight =	"";
	if(!isDigit(strDotLeft))	  strDotLeft  =	"0";
	if(!isDigit(strDotRight))	  strDotRight =	"00000000000000000000";
    if( strDotRight.length < nLen )
    {
        while( strDotRight.length < nLen )
        {
            strDotRight = strDotRight + "0";
        }
    }
	return strDotLeft +	"."	+ strDotRight.substring(0, nLen);
}
//����(float��)�� ������ �´���	���Ѵ�.
function isFloat(str)
{
	var	strDotRight	= "";
	var	strDotLeft	= "";

	if(str.indexOf(".")	!= -1)	  strDotLeft  =	str.substring(0, str.indexOf("."));
	else						  strDotLeft  =	str;

	if(str.indexOf(".")	!= -1)	  strDotRight =	str.substring(str.indexOf(".")+1, str.length);
	else						  strDotRight =	"";

	if(!isDigit(strDotRight))	  strDotRight += "0000000000";

	if(!isDigit(strDotRight) ||	!isDigit(strDotLeft)) return false;

	return true;
}

//üũ�ڽ� ��ü �����Ѵ�
function allCheck(theform) 
{
	for( var i=0; i<theform.elements.length; i++) {
		var ele = theform.elements[i];
		if( (ele.type == 'checkbox') || (ele.type == 'CHECKBOX') )
			ele.checked = true;
	}
	return;
}
//üũ�ڽ� ��ü �����Ѵ�
function disCheck(theform)
{
	for( var i=0; i<theform.elements.length; i++) {
		var ele = theform.elements[i];
		if( (ele.type == 'checkbox') || (ele.type == 'CHECKBOX') )
			ele.checked = false;
	}
	return;
}
//���� ����
function revCheck(theform) {
	for( var i=0; i<theform.elements.length; i++) {
		var ele = theform.elements[i];
		if( (ele.type == 'checkbox') || (ele.type == 'CHECKBOX') )
			ele.checked = !ele.checked;
	}
	return;
}

//���ڿ� ��±��� ����
function cutStr(str,limit){
  var tmpStr = str;
  var byte_count = 0;
  var len = str.length;
  var dot = "";
  
  for(i=0; i<len; i++){
    byte_count += chr_byte(str.charAt(i)); 
    if(byte_count == limit-1){
      if(chr_byte(str.charAt(i+1)) == 2){
        tmpStr = str.substring(0,i+1);
        dot = "...";
      }else {
        if(i+2 != len) dot = "...";
        tmpStr = str.substring(0,i+2);
      }
      break;
    }else if(byte_count == limit){
      if(i+1 != len) dot = "...";
      tmpStr = str.substring(0,i+1);
      break;
    }
  }
  document.writeln(tmpStr+dot);
  return true;
}

function chr_byte(chr){
  if(escape(chr).length > 4)
    return 2;
  else
    return 1;
}
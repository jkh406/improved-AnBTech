// 트리 +,- 변환

	function show(DeptCD, DeptCnt, DeptLast){
		Dept_show = eval(DeptCD + ".style")
		Dept_img = eval("img_"+DeptCD)
		Dept = eval(DeptCD)
	
		if(Dept.innerHTML == "")
			return false
		else{
			if(Dept_show.display == ""){
				Dept_show.display = "none"
				if(DeptCnt > 0){
					if(DeptLast == "y"){
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2.gif align=absmiddle>"
					}
					else{
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2.gif align=absmiddle>"
					}
				}
				else{
					if(DeptLast == "y"){
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2.gif align=absmiddle>"
					}
					else{
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2.gif align=absmiddle>"
					}
				}
			}
			else{
				Dept_show.display = ""

				if(DeptCnt > 0){
					if(DeptLast == "y"){
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2_2.gif border=0 align=absmiddle>"
					}
					else{
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2_2.gif border=0 align=absmiddle>"
					}
				}
				else{			
					if(DeptLast == "y"){
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2_2.gif border=0 align=absmiddle>"
					}
					else{
						Dept_img.innerHTML = "<img src=../Images/Body/Skin/tree_tep_level2_2.gif border=0 align=absmiddle>"
					}
				}
			}
		}
	}
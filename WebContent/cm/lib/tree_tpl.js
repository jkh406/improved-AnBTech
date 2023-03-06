/*
	Feel free to use your custom icons for the tree. Make sure they are all of the same size.
	User icons collections are welcome, we'll publish them giving all regards.
*/

var img_url = '../cm/admin/icons/';
var tree_tpl = {
	'target'  : 'content',	// name of the frame links will be opened in
							// other possible values are: _blank, _parent, _search, _self and _top

	'icon_e'  : img_url + 'empty.gif', // empty image
	'icon_l'  : img_url + 'line.gif',  // vertical line
	
	'icon_48' : img_url + 'base.gif',   // root icon normal
	'icon_52' : img_url + 'base.gif',   // root icon selected
	'icon_56' : img_url + 'base.gif',   // root icon opened
	'icon_60' : img_url + 'base.gif',   // root icon selected
	
	'icon_16' : img_url + 'folder.gif', // node icon normal
	'icon_20' : img_url + 'folderopen.gif', // node icon selected
	'icon_24' : img_url + 'folder.gif', // node icon opened
	'icon_28' : img_url + 'folderopen.gif', // node icon selected opened

	'icon_0'  : img_url + 'page.gif', // leaf icon normal
	'icon_4'  : img_url + 'page.gif', // leaf icon selected
	'icon_8'  : img_url + 'page.gif', // leaf icon opened
	'icon_12' : img_url + 'page.gif', // leaf icon selected
	
	'icon_2'  : img_url + 'joinbottom.gif', // junction for leaf
	'icon_3'  : img_url + 'join.gif',       // junction for last leaf
	'icon_18' : img_url + 'plusbottom.gif', // junction for closed node
	'icon_19' : img_url + 'plus.gif',       // junctioin for last closed node
	'icon_26' : img_url + 'minusbottom.gif',// junction for opened node
	'icon_27' : img_url + 'minus.gif'       // junctioin for last opended node
};


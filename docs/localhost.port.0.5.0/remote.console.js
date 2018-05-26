function togglePM(src) {
	var x = src.nextElementSibling; 
	if (src.innerHTML == '+') { 
		src.innerHTML = '-'; 
		x.style.display = 'block';
	}
	else {
		src.innerHTML = '+';
		x.style.display = 'none'; 
		return; 
	}  
}

function modifiersToString(mod) {
	var str = '';
	if (mod &  1) str='public ';
	if (mod &  2) str='private ';
	if (mod &  4) str='protected ';
	if (mod &  8) str+='static ';
	if (mod & 16) str+='final ';
	if (mod &128) str+='transient ';
	if (mod & 64) str+='volatile '; 
	return str; 
}

function render(oj) {
	if (oj.lines == null) {
		// it's a leaf.
		return ''+oj
	}
	var str ='';
	str+= 'class '+oj.name+'@'+oj.hash + '{';
	str+='<div class="Indent">';
	for (var i=0;i<oj.lines.length;i++) {
		var ln = oj.lines[i];
		if (ln.val || ln.arr) {
			str+='<span class="PlusMinus" onclick="togglePM(this)">+</span>';
		} else {
			str+=' ';
		}
		
		
		if (ln.type == 'String') {
			str+=modifiersToString(ln.modifiers)+ln.type+' '+ln.name+' = '+JSON.stringify(ln.str)+';';
		} else {
			str+=modifiersToString(ln.modifiers)+ln.type+' '+ln.name+' = '+ln.str+';';	
		}
		
		str+='\n';
		if (ln.val) {
			str+='<div class="Indent" style="display:none">';
			str+= render(ln.val);
			str+='</div>'
		} else if (ln.arr) {
			str+='<div class="Indent" style="display:none">{\n';
			for (var j=0;j<ln.arr.length;j++) {
				
				// In an array we want to have the expand option for any non-leaf.
				// for example {1,2, "hello", +class HashMap$3483 }   <<< the hashmap is a non-leaf.
				if (ln.arr[j].lines == null) {
					str+= render(ln.arr[j]);
				} else {
					str+='<span class="PlusMinus" onclick="togglePM(this)">+</span>'+ln.arr[j].name+'@'+ln.arr[j].hash;
					str+='<div class="Indent" style="display:none">';
					str+= render(ln.arr[j]);	
					str+='</div>'
				}
				
				str+=',\n';
			}
			str+='};</div>'
		}
	}
	str+='</div>'
	str+='}';
	return str;
}





function expandAll() {
	var arrayOfTogglePMs =  [].slice.call(document.getElementsByClassName('PlusMinus'));
	for (var i=0;i<arrayOfTogglePMs.length;i++) {
		if (arrayOfTogglePMs[i].innerHTML == '+') { togglePM(arrayOfTogglePMs[i]); }
	}
}

function collapseAll() {
	var arrayOfTogglePMs =  [].slice.call(document.getElementsByClassName('PlusMinus'));
	for (var i=0;i<arrayOfTogglePMs.length;i++) {
		if (arrayOfTogglePMs[i].innerHTML == '-') { togglePM(arrayOfTogglePMs[i]); }
	}
}





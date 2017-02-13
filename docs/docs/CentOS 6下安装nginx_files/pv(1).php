var html_doc = document.getElementsByTagName('head')[0];
var s = document.createElement("script");
var referer = document.referrer; 
referer = escape(String(referer));
var loc = document.location;
loc = escape(String(loc));
s.src = "/pv.php?stat=1&referer=" + referer + "&url=" + loc + "&r=" + Math.random();
html_doc.appendChild(s);
//>>built
define("dstore/Request","dojo/request dojo/_base/lang dojo/_base/array dojo/json dojo/_base/declare ./Store ./QueryResults".split(" "),function(l,f,d,m,n,p,g){var h=[].push;return n(p,{constructor:function(){this.headers||(this.headers={});this._targetContainsQueryString=0<=this.target.lastIndexOf("?")},headers:{},parse:m.parse,target:"",ascendingPrefix:"+",descendingPrefix:"-",accepts:"application/json",fetch:function(){var a=this._request();return new g(a.data,{response:a.response})},fetchRange:function(a){var b=
a.start;a=a.end;var c={};this.useRangeHeaders?c.headers=this._renderRangeHeaders(b,a):c.queryParams=this._renderRangeParams(b,a);b=this._request(c);return new g(b.data,{totalLength:b.total,response:b.response})},_request:function(a){a=a||{};var b=f.delegate(this.headers,{Accept:this.accepts});"headers"in a&&f.mixin(b,a.headers);var c=this._renderQueryParams(),e=this.target;"queryParams"in a&&h.apply(c,a.queryParams);0<c.length&&(e+=(this._targetContainsQueryString?"\x26":"?")+c.join("\x26"));a=l(e,
{method:"GET",headers:b});var k=this;return{data:a.then(function(a){a=k.parse(a);a=a.items||a;for(var b=0,c=a.length;b<c;b++)a[b]=k._restore(a[b],!0);return a}),total:a.response.then(function(a){var b=a.data.total;return-1<b?b:(a=a.getHeader("Content-Range"))&&(a=a.match(/\/(.*)/))&&+a[1]}),response:a.response}},_renderFilterParams:function(a){var b=a.type,c=a.args;return!b?[""]:"string"===b?[c[0]]:"and"===b||"or"===b?[d.map(a.args,function(a){var c=this._renderFilterParams(a);return("and"===a.type||
"or"===a.type)&&a.type!==b?"("+c+")":c},this).join("and"===b?"\x26":"|")]:[encodeURIComponent(c[0])+"\x3d"+("eq"===b?"":b+"\x3d")+encodeURIComponent(c[1])]},_renderSortParams:function(a){a=d.map(a,function(a){return(a.descending?this.descendingPrefix:this.ascendingPrefix)+encodeURIComponent(a.property)},this);var b=[];a&&b.push(this.sortParam?encodeURIComponent(this.sortParam)+"\x3d"+a:"sort("+a+")");return b},_renderRangeParams:function(a,b){var c=[];this.rangeStartParam?c.push(this.rangeStartParam+
"\x3d"+a,this.rangeCountParam+"\x3d"+(b-a)):c.push("limit("+(b-a)+(a?","+a:"")+")");return c},_renderQueryParams:function(){var a=[];d.forEach(this.queryLog,function(b){var c=b.type,c="_render"+c[0].toUpperCase()+c.substr(1)+"Params";this[c]&&h.apply(a,this[c].apply(this,b.normalizedArguments))},this);return a},_renderUrl:function(){var a=this._renderQueryParams(),b=this.target;0<a.length&&(b+="?"+a.join("\x26"));return b},_renderRangeHeaders:function(a,b){var c="items\x3d"+a+"-"+(b-1);return{Range:c,
"X-Range":c}}})});
//# sourceMappingURL=Request.js.map
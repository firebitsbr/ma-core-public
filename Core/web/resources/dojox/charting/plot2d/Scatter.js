//>>built
define("dojox/charting/plot2d/Scatter","dojo/_base/lang dojo/_base/array dojo/_base/declare dojo/has ./CartesianBase ./_PlotEvents ./common dojox/lang/functional dojox/lang/functional/reversed dojox/lang/utils dojox/gfx/fx dojox/gfx/gradutils".split(" "),function(p,q,A,B,C,D,x,E,F,r,G,H){var I=F.lambda("item.purgeGroup()");return A("dojox.charting.plot2d.Scatter",[C,D],{defaultParams:{shadows:null,animate:null},optionalParams:{markerStroke:{},markerOutline:{},markerShadow:{},markerFill:{},markerFont:"",
markerFontColor:"",styleFunc:null},constructor:function(g,d){this.opt=p.clone(p.mixin(this.opt,this.defaultParams));r.updateWithObject(this.opt,d);r.updateWithPattern(this.opt,d,this.optionalParams);this.animate=this.opt.animate},render:function(g,d){if(this.zoom&&!this.isDataDirty())return this.performZoom(g,d);this.resetEvents();this.dirty=this.isDirty();var k;this.dirty&&(q.forEach(this.series,I),this._eventSeries={},this.cleanGroup(),k=this.getGroup(),E.forEachRev(this.series,function(a){a.cleanGroup(k)}));
for(var s=this.chart.theme,p=this.events(),v=this.series.length-1;0<=v;--v){var a=this.series[v];if(!this.dirty&&!a.dirty)s.skip(),this._reconnectEvents(a.name);else if(a.cleanGroup(),a.data.length){var w=s.next("marker",[this.opt,a]),e,r=this._hScaler.scaler.getTransformerFromModel(this._hScaler),y=this._vScaler.scaler.getTransformerFromModel(this._vScaler);k=a.group;e="number"==typeof a.data[0]?q.map(a.data,function(a,b){return{x:r(b+1)+d.l,y:g.height-d.b-y(a)}},this):q.map(a.data,function(a,b){return{x:r(a.x)+
d.l,y:g.height-d.b-y(a.y)}},this);var t=Array(e.length),h=Array(e.length),u=Array(e.length);q.forEach(e,function(n,b){var f=a.data[b],c;this.opt.styleFunc||"number"!=typeof f?(c="number"!=typeof f?[f]:[],this.opt.styleFunc&&c.push(this.opt.styleFunc(f)),c=s.addMixin(w,"marker",c,!0)):c=s.post(w,"marker");var e="M"+n.x+" "+n.y+" "+c.symbol;c.marker.shadow&&(t[b]=k.createPath("M"+(n.x+c.marker.shadow.dx)+" "+(n.y+c.marker.shadow.dy)+" "+c.symbol).setStroke(c.marker.shadow).setFill(c.marker.shadow.color),
this.animate&&this._animateScatter(t[b],g.height-d.b));if(c.marker.outline){var l=x.makeStroke(c.marker.outline);l.width=2*l.width+c.marker.stroke.width;u[b]=k.createPath(e).setStroke(l);this.animate&&this._animateScatter(u[b],g.height-d.b)}var l=x.makeStroke(c.marker.stroke),m=this._plotFill(c.marker.fill,g,d);if(m&&("linear"===m.type||"radial"==m.type))m=H.getColor(m,{x:n.x,y:n.y}),l&&(l.color=m);h[b]=k.createPath(e).setStroke(l).setFill(m);this.opt.labels&&(e=h[b].getBoundingBox(),this.createLabel(k,
f,e,c));this.animate&&this._animateScatter(h[b],g.height-d.b)},this);h.length&&(a.dyn.marker=w.symbol,a.dyn.markerStroke=h[h.length-1].getStroke(),a.dyn.markerFill=h[h.length-1].getFill());if(p){var z=Array(h.length);q.forEach(h,function(d,b){var f={element:"marker",index:b,run:a,shape:d,outline:u&&u[b]||null,shadow:t&&t[b]||null,cx:e[b].x,cy:e[b].y};"number"==typeof a.data[0]?(f.x=b+1,f.y=a.data[b]):(f.x=a.data[b].x,f.y=a.data[b].y);this._connectEvents(f);z[b]=f},this);this._eventSeries[a.name]=
z}else delete this._eventSeries[a.name];a.dirty=!1}else a.dirty=!1,s.skip()}this.dirty=!1;B("dojo-bidi")&&this._checkOrientation(this.group,g,d);return this},_animateScatter:function(g,d){G.animateTransform(p.delegate({shape:g,duration:1200,transform:[{name:"translate",start:[0,d],end:[0,0]},{name:"scale",start:[0,0],end:[1,1]},{name:"original"}]},this.animate)).play()}})});
//@ sourceMappingURL=Scatter.js.map
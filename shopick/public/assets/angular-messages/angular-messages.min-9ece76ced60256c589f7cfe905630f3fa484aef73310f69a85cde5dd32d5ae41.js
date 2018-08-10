!function(e,t){"use strict";function n(){return["$animate",function(e){return{restrict:"AE",transclude:"element",priority:1,terminal:!0,require:"^^ngMessages",link:function(t,n,i,o,a){var s,c=n[0],l=i.ngMessage||i.when;i=i.ngMessageExp||i.whenExp;var d=function(e){s=e?r(e)?e:e.split(/[\s,]+/):null,o.reRender()};i?(d(t.$eval(i)),t.$watchCollection(i,d)):d(l);var u,m;o.register(c,m={test:function(e){var t=s;return e=t?r(t)?0<=t.indexOf(e):t.hasOwnProperty(e):void 0},attach:function(){u||a(function(t,r){e.enter(t,null,n),u=t;var i=u.$$attachId=o.getAttachId();u.on("$destroy",function(){u&&u.$$attachId===i&&(o.deregister(c),m.detach()),r.$destroy()})})},detach:function(){if(u){var t=u;u=null,e.leave(t)}}})}}}]}var r=t.isArray,i=t.forEach,o=t.isString,a=t.element;t.module("ngMessages",[]).directive("ngMessages",["$animate",function(e){function t(e,t){return o(t)&&0===t.length||n(e.$eval(t))}function n(e){return o(e)?e.length:!!e}return{require:"ngMessages",restrict:"AE",controller:["$element","$scope","$attrs",function(r,o,a){function s(e,t){for(var n=t,r=[];n&&n!==e;){var i=n.$$ngMessageNode;if(i&&i.length)return f[i];n.childNodes.length&&-1===r.indexOf(n)?(r.push(n),n=n.childNodes[n.childNodes.length-1]):n.previousSibling?n=n.previousSibling:(n=n.parentNode,r.push(n))}}var c=this,l=0,d=0;this.getAttachId=function(){return d++};var u,m,f=this.messages={};this.render=function(s){s=s||{},u=!1,m=s;for(var l=t(o,a.ngMessagesMultiple)||t(o,a.multiple),d=[],f={},p=c.head,h=!1,g=0;null!=p;){g++;var v=p.message,b=!1;h||i(s,function(e,t){!b&&n(e)&&v.test(t)&&!f[t]&&(b=f[t]=!0,v.attach())}),b?h=!l:d.push(v),p=p.next}i(d,function(e){e.detach()}),d.length!==g?e.setClass(r,"ng-active","ng-inactive"):e.setClass(r,"ng-inactive","ng-active")},o.$watchCollection(a.ngMessages||a["for"],c.render),r.on("$destroy",function(){i(f,function(e){e.message.detach()})}),this.reRender=function(){u||(u=!0,o.$evalAsync(function(){u&&m&&c.render(m)}))},this.register=function(e,t){var n=l.toString();f[n]={message:t};var i=r[0],o=f[n];c.head?(i=s(i,e))?(o.next=i.next,i.next=o):(o.next=c.head,c.head=o):c.head=o,e.$$ngMessageNode=n,l++,c.reRender()},this.deregister=function(e){var t=e.$$ngMessageNode;delete e.$$ngMessageNode;var n=f[t];(e=s(r[0],e))?e.next=n.next:c.head=n.next,delete f[t],c.reRender()}}]}}]).directive("ngMessagesInclude",["$templateRequest","$document","$compile",function(e,t,n){function r(e,r){var i=n.$$createComment?n.$$createComment("ngMessagesInclude",r):t[0].createComment(" ngMessagesInclude: "+r+" "),i=a(i);e.after(i),e.remove()}return{restrict:"AE",require:"^^ngMessages",link:function(t,i,a){var s=a.ngMessagesInclude||a.src;e(s).then(function(e){t.$$destroyed||(o(e)&&!e.trim()?r(i,s):n(e)(t,function(e){i.after(e),r(i,s)}))})}}}]).directive("ngMessage",n()).directive("ngMessageExp",n())}(window,window.angular);
var app=function(){"use strict";function e(){}function t(e){return e()}function n(){return Object.create(null)}function r(e){e.forEach(t)}function o(e){return"function"==typeof e}function s(e,t){return e!=e?t==t:e!==t||e&&"object"==typeof e||"function"==typeof e}function i(t,n,r){t.$$.on_destroy.push(function(t,...n){if(null==t)return e;const r=t.subscribe(...n);return r.unsubscribe?()=>r.unsubscribe():r}(n,r))}function a(e,t){e.appendChild(t)}function u(e,t,n){e.insertBefore(t,n||null)}function c(e){e.parentNode.removeChild(e)}function l(e){return document.createElement(e)}function f(e){return document.createTextNode(e)}function d(){return f(" ")}function p(){return f("")}function m(e,t,n,r){return e.addEventListener(t,n,r),()=>e.removeEventListener(t,n,r)}function h(e){return function(t){return t.preventDefault(),e.call(this,t)}}function g(e,t,n){null==n?e.removeAttribute(t):e.getAttribute(t)!==n&&e.setAttribute(t,n)}function v(e,t){t=""+t,e.wholeText!==t&&(e.data=t)}function y(e,t){e.value=null==t?"":t}function w(e,t){for(let n=0;n<e.options.length;n+=1){const r=e.options[n];if(r.__value===t)return void(r.selected=!0)}}let $;function b(e){$=e}function x(){const e=function(){if(!$)throw new Error("Function called outside component initialization");return $}();return(t,n)=>{const r=e.$$.callbacks[t];if(r){const o=function(e,t){const n=document.createEvent("CustomEvent");return n.initCustomEvent(e,!1,!1,t),n}(t,n);r.slice().forEach((t=>{t.call(e,o)}))}}}const E=[],_=[],C=[],S=[],O=Promise.resolve();let k=!1;function A(e){C.push(e)}let j=!1;const R=new Set;function N(){if(!j){j=!0;do{for(let e=0;e<E.length;e+=1){const t=E[e];b(t),U(t.$$)}for(b(null),E.length=0;_.length;)_.pop()();for(let e=0;e<C.length;e+=1){const t=C[e];R.has(t)||(R.add(t),t())}C.length=0}while(E.length);for(;S.length;)S.pop()();k=!1,j=!1,R.clear()}}function U(e){if(null!==e.fragment){e.update(),r(e.before_update);const t=e.dirty;e.dirty=[-1],e.fragment&&e.fragment.p(e.ctx,t),e.after_update.forEach(A)}}const T=new Set;let P;function B(){P={r:0,c:[],p:P}}function L(){P.r||r(P.c),P=P.p}function I(e,t){e&&e.i&&(T.delete(e),e.i(t))}function M(e,t,n,r){if(e&&e.o){if(T.has(e))return;T.add(e),P.c.push((()=>{T.delete(e),r&&(n&&e.d(1),r())})),e.o(t)}}function q(e,t){M(e,1,1,(()=>{t.delete(e.key)}))}function D(e,t,n,r,o,s,i,a,u,c,l,f){let d=e.length,p=s.length,m=d;const h={};for(;m--;)h[e[m].key]=m;const g=[],v=new Map,y=new Map;for(m=p;m--;){const e=f(o,s,m),a=n(e);let u=i.get(a);u?r&&u.p(e,t):(u=c(a,e),u.c()),v.set(a,g[m]=u),a in h&&y.set(a,Math.abs(m-h[a]))}const w=new Set,$=new Set;function b(e){I(e,1),e.m(a,l),i.set(e.key,e),l=e.first,p--}for(;d&&p;){const t=g[p-1],n=e[d-1],r=t.key,o=n.key;t===n?(l=t.first,d--,p--):v.has(o)?!i.has(r)||w.has(r)?b(t):$.has(o)?d--:y.get(r)>y.get(o)?($.add(r),b(t)):(w.add(o),d--):(u(n,i),d--)}for(;d--;){const t=e[d];v.has(t.key)||u(t,i)}for(;p;)b(g[p-1]);return g}function F(e){e&&e.c()}function z(e,n,s){const{fragment:i,on_mount:a,on_destroy:u,after_update:c}=e.$$;i&&i.m(n,s),A((()=>{const n=a.map(t).filter(o);u?u.push(...n):r(n),e.$$.on_mount=[]})),c.forEach(A)}function H(e,t){const n=e.$$;null!==n.fragment&&(r(n.on_destroy),n.fragment&&n.fragment.d(t),n.on_destroy=n.fragment=null,n.ctx=[])}function V(e,t){-1===e.$$.dirty[0]&&(E.push(e),k||(k=!0,O.then(N)),e.$$.dirty.fill(0)),e.$$.dirty[t/31|0]|=1<<t%31}function W(t,o,s,i,a,u,l=[-1]){const f=$;b(t);const d=o.props||{},p=t.$$={fragment:null,ctx:null,props:u,update:e,not_equal:a,bound:n(),on_mount:[],on_destroy:[],before_update:[],after_update:[],context:new Map(f?f.$$.context:[]),callbacks:n(),dirty:l,skip_bound:!1};let m=!1;if(p.ctx=s?s(t,d,((e,n,...r)=>{const o=r.length?r[0]:n;return p.ctx&&a(p.ctx[e],p.ctx[e]=o)&&(!p.skip_bound&&p.bound[e]&&p.bound[e](o),m&&V(t,e)),n})):[],p.update(),m=!0,r(p.before_update),p.fragment=!!i&&i(p.ctx),o.target){if(o.hydrate){const e=function(e){return Array.from(e.childNodes)}(o.target);p.fragment&&p.fragment.l(e),e.forEach(c)}else p.fragment&&p.fragment.c();o.intro&&I(t.$$.fragment),z(t,o.target,o.anchor),N()}b(f)}class J{$destroy(){H(this,1),this.$destroy=e}$on(e,t){const n=this.$$.callbacks[e]||(this.$$.callbacks[e]=[]);return n.push(t),()=>{const e=n.indexOf(t);-1!==e&&n.splice(e,1)}}$set(e){var t;this.$$set&&(t=e,0!==Object.keys(t).length)&&(this.$$.skip_bound=!0,this.$$set(e),this.$$.skip_bound=!1)}}var X=function(e,t){return function(){for(var n=new Array(arguments.length),r=0;r<n.length;r++)n[r]=arguments[r];return e.apply(t,n)}},K=Object.prototype.toString;function G(e){return"[object Array]"===K.call(e)}function Q(e){return void 0===e}function Y(e){return null!==e&&"object"==typeof e}function Z(e){if("[object Object]"!==K.call(e))return!1;var t=Object.getPrototypeOf(e);return null===t||t===Object.prototype}function ee(e){return"[object Function]"===K.call(e)}function te(e,t){if(null!=e)if("object"!=typeof e&&(e=[e]),G(e))for(var n=0,r=e.length;n<r;n++)t.call(null,e[n],n,e);else for(var o in e)Object.prototype.hasOwnProperty.call(e,o)&&t.call(null,e[o],o,e)}var ne={isArray:G,isArrayBuffer:function(e){return"[object ArrayBuffer]"===K.call(e)},isBuffer:function(e){return null!==e&&!Q(e)&&null!==e.constructor&&!Q(e.constructor)&&"function"==typeof e.constructor.isBuffer&&e.constructor.isBuffer(e)},isFormData:function(e){return"undefined"!=typeof FormData&&e instanceof FormData},isArrayBufferView:function(e){return"undefined"!=typeof ArrayBuffer&&ArrayBuffer.isView?ArrayBuffer.isView(e):e&&e.buffer&&e.buffer instanceof ArrayBuffer},isString:function(e){return"string"==typeof e},isNumber:function(e){return"number"==typeof e},isObject:Y,isPlainObject:Z,isUndefined:Q,isDate:function(e){return"[object Date]"===K.call(e)},isFile:function(e){return"[object File]"===K.call(e)},isBlob:function(e){return"[object Blob]"===K.call(e)},isFunction:ee,isStream:function(e){return Y(e)&&ee(e.pipe)},isURLSearchParams:function(e){return"undefined"!=typeof URLSearchParams&&e instanceof URLSearchParams},isStandardBrowserEnv:function(){return("undefined"==typeof navigator||"ReactNative"!==navigator.product&&"NativeScript"!==navigator.product&&"NS"!==navigator.product)&&("undefined"!=typeof window&&"undefined"!=typeof document)},forEach:te,merge:function e(){var t={};function n(n,r){Z(t[r])&&Z(n)?t[r]=e(t[r],n):Z(n)?t[r]=e({},n):G(n)?t[r]=n.slice():t[r]=n}for(var r=0,o=arguments.length;r<o;r++)te(arguments[r],n);return t},extend:function(e,t,n){return te(t,(function(t,r){e[r]=n&&"function"==typeof t?X(t,n):t})),e},trim:function(e){return e.replace(/^\s*/,"").replace(/\s*$/,"")},stripBOM:function(e){return 65279===e.charCodeAt(0)&&(e=e.slice(1)),e}};function re(e){return encodeURIComponent(e).replace(/%3A/gi,":").replace(/%24/g,"$").replace(/%2C/gi,",").replace(/%20/g,"+").replace(/%5B/gi,"[").replace(/%5D/gi,"]")}var oe=function(e,t,n){if(!t)return e;var r;if(n)r=n(t);else if(ne.isURLSearchParams(t))r=t.toString();else{var o=[];ne.forEach(t,(function(e,t){null!=e&&(ne.isArray(e)?t+="[]":e=[e],ne.forEach(e,(function(e){ne.isDate(e)?e=e.toISOString():ne.isObject(e)&&(e=JSON.stringify(e)),o.push(re(t)+"="+re(e))})))})),r=o.join("&")}if(r){var s=e.indexOf("#");-1!==s&&(e=e.slice(0,s)),e+=(-1===e.indexOf("?")?"?":"&")+r}return e};function se(){this.handlers=[]}se.prototype.use=function(e,t){return this.handlers.push({fulfilled:e,rejected:t}),this.handlers.length-1},se.prototype.eject=function(e){this.handlers[e]&&(this.handlers[e]=null)},se.prototype.forEach=function(e){ne.forEach(this.handlers,(function(t){null!==t&&e(t)}))};var ie=se,ae=function(e,t,n){return ne.forEach(n,(function(n){e=n(e,t)})),e},ue=function(e){return!(!e||!e.__CANCEL__)},ce=function(e,t){ne.forEach(e,(function(n,r){r!==t&&r.toUpperCase()===t.toUpperCase()&&(e[t]=n,delete e[r])}))},le=function(e,t,n,r,o){return function(e,t,n,r,o){return e.config=t,n&&(e.code=n),e.request=r,e.response=o,e.isAxiosError=!0,e.toJSON=function(){return{message:this.message,name:this.name,description:this.description,number:this.number,fileName:this.fileName,lineNumber:this.lineNumber,columnNumber:this.columnNumber,stack:this.stack,config:this.config,code:this.code}},e}(new Error(e),t,n,r,o)},fe=ne.isStandardBrowserEnv()?{write:function(e,t,n,r,o,s){var i=[];i.push(e+"="+encodeURIComponent(t)),ne.isNumber(n)&&i.push("expires="+new Date(n).toGMTString()),ne.isString(r)&&i.push("path="+r),ne.isString(o)&&i.push("domain="+o),!0===s&&i.push("secure"),document.cookie=i.join("; ")},read:function(e){var t=document.cookie.match(new RegExp("(^|;\\s*)("+e+")=([^;]*)"));return t?decodeURIComponent(t[3]):null},remove:function(e){this.write(e,"",Date.now()-864e5)}}:{write:function(){},read:function(){return null},remove:function(){}},de=["age","authorization","content-length","content-type","etag","expires","from","host","if-modified-since","if-unmodified-since","last-modified","location","max-forwards","proxy-authorization","referer","retry-after","user-agent"],pe=ne.isStandardBrowserEnv()?function(){var e,t=/(msie|trident)/i.test(navigator.userAgent),n=document.createElement("a");function r(e){var r=e;return t&&(n.setAttribute("href",r),r=n.href),n.setAttribute("href",r),{href:n.href,protocol:n.protocol?n.protocol.replace(/:$/,""):"",host:n.host,search:n.search?n.search.replace(/^\?/,""):"",hash:n.hash?n.hash.replace(/^#/,""):"",hostname:n.hostname,port:n.port,pathname:"/"===n.pathname.charAt(0)?n.pathname:"/"+n.pathname}}return e=r(window.location.href),function(t){var n=ne.isString(t)?r(t):t;return n.protocol===e.protocol&&n.host===e.host}}():function(){return!0},me=function(e){return new Promise((function(t,n){var r=e.data,o=e.headers;ne.isFormData(r)&&delete o["Content-Type"];var s=new XMLHttpRequest;if(e.auth){var i=e.auth.username||"",a=e.auth.password?unescape(encodeURIComponent(e.auth.password)):"";o.Authorization="Basic "+btoa(i+":"+a)}var u,c,l=(u=e.baseURL,c=e.url,u&&!/^([a-z][a-z\d\+\-\.]*:)?\/\//i.test(c)?function(e,t){return t?e.replace(/\/+$/,"")+"/"+t.replace(/^\/+/,""):e}(u,c):c);if(s.open(e.method.toUpperCase(),oe(l,e.params,e.paramsSerializer),!0),s.timeout=e.timeout,s.onreadystatechange=function(){if(s&&4===s.readyState&&(0!==s.status||s.responseURL&&0===s.responseURL.indexOf("file:"))){var r,o,i,a,u,c="getAllResponseHeaders"in s?(r=s.getAllResponseHeaders(),u={},r?(ne.forEach(r.split("\n"),(function(e){if(a=e.indexOf(":"),o=ne.trim(e.substr(0,a)).toLowerCase(),i=ne.trim(e.substr(a+1)),o){if(u[o]&&de.indexOf(o)>=0)return;u[o]="set-cookie"===o?(u[o]?u[o]:[]).concat([i]):u[o]?u[o]+", "+i:i}})),u):u):null,l={data:e.responseType&&"text"!==e.responseType?s.response:s.responseText,status:s.status,statusText:s.statusText,headers:c,config:e,request:s};!function(e,t,n){var r=n.config.validateStatus;n.status&&r&&!r(n.status)?t(le("Request failed with status code "+n.status,n.config,null,n.request,n)):e(n)}(t,n,l),s=null}},s.onabort=function(){s&&(n(le("Request aborted",e,"ECONNABORTED",s)),s=null)},s.onerror=function(){n(le("Network Error",e,null,s)),s=null},s.ontimeout=function(){var t="timeout of "+e.timeout+"ms exceeded";e.timeoutErrorMessage&&(t=e.timeoutErrorMessage),n(le(t,e,"ECONNABORTED",s)),s=null},ne.isStandardBrowserEnv()){var f=(e.withCredentials||pe(l))&&e.xsrfCookieName?fe.read(e.xsrfCookieName):void 0;f&&(o[e.xsrfHeaderName]=f)}if("setRequestHeader"in s&&ne.forEach(o,(function(e,t){void 0===r&&"content-type"===t.toLowerCase()?delete o[t]:s.setRequestHeader(t,e)})),ne.isUndefined(e.withCredentials)||(s.withCredentials=!!e.withCredentials),e.responseType)try{s.responseType=e.responseType}catch(t){if("json"!==e.responseType)throw t}"function"==typeof e.onDownloadProgress&&s.addEventListener("progress",e.onDownloadProgress),"function"==typeof e.onUploadProgress&&s.upload&&s.upload.addEventListener("progress",e.onUploadProgress),e.cancelToken&&e.cancelToken.promise.then((function(e){s&&(s.abort(),n(e),s=null)})),r||(r=null),s.send(r)}))},he={"Content-Type":"application/x-www-form-urlencoded"};function ge(e,t){!ne.isUndefined(e)&&ne.isUndefined(e["Content-Type"])&&(e["Content-Type"]=t)}var ve,ye={adapter:(("undefined"!=typeof XMLHttpRequest||"undefined"!=typeof process&&"[object process]"===Object.prototype.toString.call(process))&&(ve=me),ve),transformRequest:[function(e,t){return ce(t,"Accept"),ce(t,"Content-Type"),ne.isFormData(e)||ne.isArrayBuffer(e)||ne.isBuffer(e)||ne.isStream(e)||ne.isFile(e)||ne.isBlob(e)?e:ne.isArrayBufferView(e)?e.buffer:ne.isURLSearchParams(e)?(ge(t,"application/x-www-form-urlencoded;charset=utf-8"),e.toString()):ne.isObject(e)?(ge(t,"application/json;charset=utf-8"),JSON.stringify(e)):e}],transformResponse:[function(e){if("string"==typeof e)try{e=JSON.parse(e)}catch(e){}return e}],timeout:0,xsrfCookieName:"XSRF-TOKEN",xsrfHeaderName:"X-XSRF-TOKEN",maxContentLength:-1,maxBodyLength:-1,validateStatus:function(e){return e>=200&&e<300}};ye.headers={common:{Accept:"application/json, text/plain, */*"}},ne.forEach(["delete","get","head"],(function(e){ye.headers[e]={}})),ne.forEach(["post","put","patch"],(function(e){ye.headers[e]=ne.merge(he)}));var we=ye;function $e(e){e.cancelToken&&e.cancelToken.throwIfRequested()}var be=function(e){return $e(e),e.headers=e.headers||{},e.data=ae(e.data,e.headers,e.transformRequest),e.headers=ne.merge(e.headers.common||{},e.headers[e.method]||{},e.headers),ne.forEach(["delete","get","head","post","put","patch","common"],(function(t){delete e.headers[t]})),(e.adapter||we.adapter)(e).then((function(t){return $e(e),t.data=ae(t.data,t.headers,e.transformResponse),t}),(function(t){return ue(t)||($e(e),t&&t.response&&(t.response.data=ae(t.response.data,t.response.headers,e.transformResponse))),Promise.reject(t)}))},xe=function(e,t){t=t||{};var n={},r=["url","method","data"],o=["headers","auth","proxy","params"],s=["baseURL","transformRequest","transformResponse","paramsSerializer","timeout","timeoutMessage","withCredentials","adapter","responseType","xsrfCookieName","xsrfHeaderName","onUploadProgress","onDownloadProgress","decompress","maxContentLength","maxBodyLength","maxRedirects","transport","httpAgent","httpsAgent","cancelToken","socketPath","responseEncoding"],i=["validateStatus"];function a(e,t){return ne.isPlainObject(e)&&ne.isPlainObject(t)?ne.merge(e,t):ne.isPlainObject(t)?ne.merge({},t):ne.isArray(t)?t.slice():t}function u(r){ne.isUndefined(t[r])?ne.isUndefined(e[r])||(n[r]=a(void 0,e[r])):n[r]=a(e[r],t[r])}ne.forEach(r,(function(e){ne.isUndefined(t[e])||(n[e]=a(void 0,t[e]))})),ne.forEach(o,u),ne.forEach(s,(function(r){ne.isUndefined(t[r])?ne.isUndefined(e[r])||(n[r]=a(void 0,e[r])):n[r]=a(void 0,t[r])})),ne.forEach(i,(function(r){r in t?n[r]=a(e[r],t[r]):r in e&&(n[r]=a(void 0,e[r]))}));var c=r.concat(o).concat(s).concat(i),l=Object.keys(e).concat(Object.keys(t)).filter((function(e){return-1===c.indexOf(e)}));return ne.forEach(l,u),n};function Ee(e){this.defaults=e,this.interceptors={request:new ie,response:new ie}}Ee.prototype.request=function(e){"string"==typeof e?(e=arguments[1]||{}).url=arguments[0]:e=e||{},(e=xe(this.defaults,e)).method?e.method=e.method.toLowerCase():this.defaults.method?e.method=this.defaults.method.toLowerCase():e.method="get";var t=[be,void 0],n=Promise.resolve(e);for(this.interceptors.request.forEach((function(e){t.unshift(e.fulfilled,e.rejected)})),this.interceptors.response.forEach((function(e){t.push(e.fulfilled,e.rejected)}));t.length;)n=n.then(t.shift(),t.shift());return n},Ee.prototype.getUri=function(e){return e=xe(this.defaults,e),oe(e.url,e.params,e.paramsSerializer).replace(/^\?/,"")},ne.forEach(["delete","get","head","options"],(function(e){Ee.prototype[e]=function(t,n){return this.request(xe(n||{},{method:e,url:t,data:(n||{}).data}))}})),ne.forEach(["post","put","patch"],(function(e){Ee.prototype[e]=function(t,n,r){return this.request(xe(r||{},{method:e,url:t,data:n}))}}));var _e=Ee;function Ce(e){this.message=e}Ce.prototype.toString=function(){return"Cancel"+(this.message?": "+this.message:"")},Ce.prototype.__CANCEL__=!0;var Se=Ce;function Oe(e){if("function"!=typeof e)throw new TypeError("executor must be a function.");var t;this.promise=new Promise((function(e){t=e}));var n=this;e((function(e){n.reason||(n.reason=new Se(e),t(n.reason))}))}Oe.prototype.throwIfRequested=function(){if(this.reason)throw this.reason},Oe.source=function(){var e;return{token:new Oe((function(t){e=t})),cancel:e}};var ke=Oe;function Ae(e){var t=new _e(e),n=X(_e.prototype.request,t);return ne.extend(n,_e.prototype,t),ne.extend(n,t),n}var je=Ae(we);je.Axios=_e,je.create=function(e){return Ae(xe(je.defaults,e))},je.Cancel=Se,je.CancelToken=ke,je.isCancel=ue,je.all=function(e){return Promise.all(e)},je.spread=function(e){return function(t){return e.apply(null,t)}};var Re=je,Ne=je;Re.default=Ne;var Ue=Re,Te="";const Pe=[];function Be(t,n=e){let r;const o=[];function i(e){if(s(t,e)&&(t=e,r)){const e=!Pe.length;for(let e=0;e<o.length;e+=1){const n=o[e];n[1](),Pe.push(n,t)}if(e){for(let e=0;e<Pe.length;e+=2)Pe[e][0](Pe[e+1]);Pe.length=0}}}return{set:i,update:function(e){i(e(t))},subscribe:function(s,a=e){const u=[s,a];return o.push(u),1===o.length&&(r=n(i)||e),s(t),()=>{const e=o.indexOf(u);-1!==e&&o.splice(e,1),0===o.length&&(r(),r=null)}}}}const{subscribe:Le,set:Ie}=Be(parseInt(window.localStorage.getItem("userId"))),Me={subscribe:Le,update:e=>{window.localStorage.setItem("userId",e),Ie(parseInt(e))},logout:()=>{window.localStorage.removeItem("userId"),Ie(null)}};let qe;async function De(){try{return(await Ue.get(Te+"/rounds/current")).data}catch(e){if(404===e.response.status)throw new Error("Rodada não foi encontrada");throw new Error("Ocorreu um erro no sistema")}}function Fe(e){let t,n,o,s,i,f,v,w,$,b,x=e[2]&&He(e);return{c(){t=l("form"),n=l("label"),n.textContent="Nome",o=d(),s=l("input"),i=d(),f=l("button"),f.textContent="Login",v=d(),x&&x.c(),w=p(),g(n,"for","user-name"),g(s,"id","user-name"),g(s,"type","text"),g(f,"type","submit")},m(r,c){u(r,t,c),a(t,n),a(t,o),a(t,s),y(s,e[1]),a(t,i),a(t,f),u(r,v,c),x&&x.m(r,c),u(r,w,c),$||(b=[m(s,"input",e[4]),m(t,"submit",h(e[3]))],$=!0)},p(e,t){2&t&&s.value!==e[1]&&y(s,e[1]),e[2]?x?x.p(e,t):(x=He(e),x.c(),x.m(w.parentNode,w)):x&&(x.d(1),x=null)},d(e){e&&c(t),e&&c(v),x&&x.d(e),e&&c(w),$=!1,r(b)}}}function ze(t){let n;return{c(){n=f("Logando ...")},m(e,t){u(e,n,t)},p:e,d(e){e&&c(n)}}}function He(e){let t,n;return{c(){t=l("p"),n=f(e[2])},m(e,r){u(e,t,r),a(t,n)},p(e,t){4&t&&v(n,e[2])},d(e){e&&c(t)}}}function Ve(t){let n;function r(e,t){return e[0]?ze:Fe}let o=r(t),s=o(t);return{c(){s.c(),n=p()},m(e,t){s.m(e,t),u(e,n,t)},p(e,[t]){o===(o=r(e))&&s?s.p(e,t):(s.d(1),s=o(e),s&&(s.c(),s.m(n.parentNode,n)))},i:e,o:e,d(e){s.d(e),e&&c(n)}}}function We(e,t,n){const r=x();let o,s,i=!1,a=!1;return[a,o,s,async function(){if(i)return;let e;try{i=!0,e=setTimeout((()=>n(0,a=!0)),150);let t=await async function(e){try{return(await Ue.get(`${Te}/users/${e}`)).data.id}catch(e){if(404===e.response.status)throw new Error("Usuário não encontrado");throw new Error("Ocorreu um erro no sistema")}}(o);r("loggedIn",t)}catch(e){n(2,s=e.message)}finally{clearTimeout(e),n(0,a=!1),i=!1}},function(){o=this.value,n(1,o)}]}Me.subscribe((e=>qe=e));class Je extends J{constructor(e){super(),W(this,e,We,Ve,s,{})}}const{subscribe:Xe,set:Ke}=Be(null,(e=>{De().then((t=>e(t)));const t=setInterval((async()=>{const n=await De();console.log({round:n}),e(n),"Watching"===n.step&&clearInterval(t)}),5e3);return()=>{clearInterval(t)}})),Ge={subscribe:Xe,forceUpdate:()=>{De().then((e=>Ke(e)))}};function Qe(e,t,n){const r=e.slice();return r[12]=t[n],r}function Ye(e){let t;return{c(){t=l("h2"),t.textContent="Muitas pessoas já viram esse filme, escolha outro"},m(e,n){u(e,t,n)},d(e){e&&c(t)}}}function Ze(e){let t,n,r,o,s=e[12]+"";return{c(){t=l("option"),n=f(s),r=d(),t.__value=o=e[12],t.value=t.__value},m(e,o){u(e,t,o),a(t,n),a(t,r)},p(e,r){16&r&&s!==(s=e[12]+"")&&v(n,s),16&r&&o!==(o=e[12])&&(t.__value=o,t.value=t.__value)},d(e){e&&c(t)}}}function et(t){let n;return{c(){n=l("button"),n.textContent="Enviando..."},m(e,t){u(e,n,t)},p:e,d(e){e&&c(n)}}}function tt(e){let t;function n(e,t){return e[0]?rt:nt}let r=n(e),o=r(e);return{c(){t=l("button"),o.c(),g(t,"type","submit"),t.disabled=e[7]},m(e,n){u(e,t,n),o.m(t,null)},p(e,s){r!==(r=n(e))&&(o.d(1),o=r(e),o&&(o.c(),o.m(t,null))),128&s&&(t.disabled=e[7])},d(e){e&&c(t),o.d()}}}function nt(e){let t;return{c(){t=f("Recommendar")},m(e,n){u(e,t,n)},d(e){e&&c(t)}}}function rt(e){let t;return{c(){t=f("Editar")},m(e,n){u(e,t,n)},d(e){e&&c(t)}}}function ot(e){let t;return{c(){t=l("p"),t.textContent="Sua recomendação já foi feita. Você pode editá-la se quiser"},m(e,n){u(e,t,n)},d(e){e&&c(t)}}}function st(e){let t,n;return{c(){t=l("p"),n=f(e[5])},m(e,r){u(e,t,r),a(t,n)},p(e,t){32&t&&v(n,e[5])},d(e){e&&c(t)}}}function it(t){let n,o,s,i,f,p,v,$,b,x,E,_,C,S,O,k=t[1]&&Ye(),j=t[4],R=[];for(let e=0;e<j.length;e+=1)R[e]=Ze(Qe(t,j,e));function N(e,t){return e[6]?et:tt}let U=N(t),T=U(t),P=t[0]&&!t[1]&&ot(),B=t[5]&&st(t);return{c(){n=l("form"),k&&k.c(),o=d(),s=l("label"),s.textContent="Título",i=d(),f=l("input"),p=d(),v=l("label"),v.textContent="Stream",$=d(),b=l("select"),x=l("option"),x.textContent="Escolha um stream";for(let e=0;e<R.length;e+=1)R[e].c();E=d(),T.c(),_=d(),P&&P.c(),C=d(),B&&B.c(),g(s,"for","movie-title"),g(f,"id","movie-title"),g(f,"autocomplete","off"),g(f,"type","text"),f.autofocus=!0,g(v,"for","movie-stream"),x.disabled=!0,x.__value="",x.value=x.__value,g(b,"id","movie-stream"),void 0===t[3]&&A((()=>t[11].call(b)))},m(e,r){u(e,n,r),k&&k.m(n,null),a(n,o),a(n,s),a(n,i),a(n,f),y(f,t[2]),a(n,p),a(n,v),a(n,$),a(n,b),a(b,x);for(let e=0;e<R.length;e+=1)R[e].m(b,null);w(b,t[3]),a(n,E),T.m(n,null),a(n,_),P&&P.m(n,null),a(n,C),B&&B.m(n,null),f.focus(),S||(O=[m(f,"input",t[9]),m(f,"change",t[10]),m(b,"change",t[11]),m(n,"submit",h(t[8]))],S=!0)},p(e,[t]){if(e[1]?k||(k=Ye(),k.c(),k.m(n,o)):k&&(k.d(1),k=null),4&t&&f.value!==e[2]&&y(f,e[2]),16&t){let n;for(j=e[4],n=0;n<j.length;n+=1){const r=Qe(e,j,n);R[n]?R[n].p(r,t):(R[n]=Ze(r),R[n].c(),R[n].m(b,null))}for(;n<R.length;n+=1)R[n].d(1);R.length=j.length}24&t&&w(b,e[3]),U===(U=N(e))&&T?T.p(e,t):(T.d(1),T=U(e),T&&(T.c(),T.m(n,_))),e[0]&&!e[1]?P||(P=ot(),P.c(),P.m(n,C)):P&&(P.d(1),P=null),e[5]?B?B.p(e,t):(B=st(e),B.c(),B.m(n,null)):B&&(B.d(1),B=null)},i:e,o:e,d(e){e&&c(n),k&&k.d(),function(e,t){for(let n=0;n<e.length;n+=1)e[n]&&e[n].d(t)}(R,e),T.d(),P&&P.d(),B&&B.d(),S=!1,r(O)}}}function at(e,t,n){let r,{userMovie:o}=t,{tooManyPeopleAlreadyWatched:s}=t,i=o?.title,a=o?.stream,u=[],c=!1;(async function(){try{return(await Ue.get(Te+"/streams")).data}catch(e){throw new Error("Ocorreu um erro no sistema")}})().then((e=>n(4,u=e))).catch((e=>n(5,r=e)));let l;return e.$$set=e=>{"userMovie"in e&&n(0,o=e.userMovie),"tooManyPeopleAlreadyWatched"in e&&n(1,s=e.tooManyPeopleAlreadyWatched)},e.$$.update=()=>{13&e.$$.dirty&&n(7,l=!a||!i||o?.title===i&&o?.stream===a)},[o,s,i,a,u,r,c,l,async function(){try{n(6,c=!0),await async function(e,t,n){try{e?await Ue.put(`${Te}/movies/${e}/`,{title:t,stream:n,userId:qe}):await Ue.post(Te+"/movies/",{title:t,stream:n,userId:qe})}catch(e){if(401===e.response.status)throw new Error("Faça login antes de recomendar um filme");if(400===e.response.status)throw new Error(e.response.data.message);throw new Error("Ocorreu um erro no sistema")}}(o?.id,i,a),n(0,o={title:i,stream:a}),Ge.forceUpdate()}catch(e){n(5,r=e.message)}finally{n(6,c=!1)}},function(){i=this.value,n(2,i)},()=>n(2,i=i?.trim()),function(){a=function(e){const t=e.querySelector(":checked")||e.options[0];return t&&t.__value}(this),n(3,a),n(4,u)}]}class ut extends J{constructor(e){super(),W(this,e,at,it,s,{userMovie:0,tooManyPeopleAlreadyWatched:1})}}function ct(e){let t,n,o,s,i,p,h,v,y,w,$,b;return{c(){t=l("div"),n=l("label"),o=l("input"),i=f("\n            Já vi 😓"),p=d(),h=l("label"),v=l("input"),w=f("\n            Ainda não vi 😀"),g(o,"type","radio"),o.__value=s=!0,o.value=o.__value,e[7][0].push(o),g(v,"type","radio"),v.__value=y=!1,v.value=v.__value,e[7][0].push(v),g(t,"class","choices svelte-s9mgri")},m(r,s){u(r,t,s),a(t,n),a(n,o),o.checked=o.__value===e[2],a(n,i),a(t,p),a(t,h),a(h,v),v.checked=v.__value===e[2],a(h,w),$||(b=[m(o,"change",e[6]),m(o,"change",e[5]),m(v,"change",e[8]),m(v,"change",e[5])],$=!0)},p(e,t){4&t&&(o.checked=o.__value===e[2]),4&t&&(v.checked=v.__value===e[2])},d(n){n&&c(t),e[7][0].splice(e[7][0].indexOf(o),1),e[7][0].splice(e[7][0].indexOf(v),1),$=!1,r(b)}}}function lt(t){let n,r,o,s,i,p,m,h,y,w,$,b,x=t[0]-1+"",E=t[1].title+"",_=t[1].stream+"",C=!t[4]&&ct(t);return{c(){n=l("div"),r=l("span"),o=f(t[3]),s=f("/"),i=f(x),p=d(),m=l("span"),h=f(E),y=d(),w=l("span"),$=f(_),b=d(),C&&C.c(),g(r,"class","vote-counter svelte-s9mgri"),g(m,"class","title svelte-s9mgri"),g(w,"class","stream svelte-s9mgri"),g(n,"class","card svelte-s9mgri")},m(e,t){u(e,n,t),a(n,r),a(r,o),a(r,s),a(r,i),a(n,p),a(n,m),a(m,h),a(n,y),a(n,w),a(w,$),a(n,b),C&&C.m(n,null)},p(e,[t]){8&t&&v(o,e[3]),1&t&&x!==(x=e[0]-1+"")&&v(i,x),2&t&&E!==(E=e[1].title+"")&&v(h,E),2&t&&_!==(_=e[1].stream+"")&&v($,_),e[4]?C&&(C.d(1),C=null):C?C.p(e,t):(C=ct(e),C.c(),C.m(n,null))},i:e,o:e,d(e){e&&c(n),C&&C.d()}}}function ft(e,t,n){let r;i(e,Me,(e=>n(9,r=e)));let{totalUsers:o}=t,{movie:s}=t,a=s?.movieVisualizations?.find((e=>e.userId===r))?.watchedBeforeRound;let u,c;return e.$$set=e=>{"totalUsers"in e&&n(0,o=e.totalUsers),"movie"in e&&n(1,s=e.movie)},e.$$.update=()=>{2&e.$$.dirty&&n(3,u=s?.movieVisualizations?.filter((e=>null!==e.watchedBeforeRound))?.length||0),514&e.$$.dirty&&n(4,c=s?.userId===r)},[o,s,a,u,c,async function(){await async function(e,t,n){try{await Ue.post(Te+"/voting",{userId:e,title:t,watched:n})}catch(e){if(401===e.response.status)throw new Error("Faça login antes de votar em um filme");if(400===e.response.status)throw new Error(e.response.data.message);throw new Error("Ocorreu um erro no sistema")}}(r,s.title,a),Ge.forceUpdate()},function(){a=this.__value,n(2,a)},[[]],function(){a=this.__value,n(2,a)}]}class dt extends J{constructor(e){super(),W(this,e,ft,lt,s,{totalUsers:0,movie:1})}}function pt(e,t,n){const r=e.slice();return r[1]=t[n],r}function mt(e,t){let n,r,o;return r=new dt({props:{movie:t[1],totalUsers:t[0].length}}),{key:e,first:null,c(){n=p(),F(r.$$.fragment),this.first=n},m(e,t){u(e,n,t),z(r,e,t),o=!0},p(e,t){const n={};1&t&&(n.movie=e[1]),1&t&&(n.totalUsers=e[0].length),r.$set(n)},i(e){o||(I(r.$$.fragment,e),o=!0)},o(e){M(r.$$.fragment,e),o=!1},d(e){e&&c(n),H(r,e)}}}function ht(e){let t,n,r,o,s=[],i=new Map,f=e[0];const p=e=>e[1].title;for(let t=0;t<f.length;t+=1){let n=pt(e,f,t),r=p(n);i.set(r,s[t]=mt(r,n))}return{c(){t=l("div"),n=l("h2"),n.textContent="Filmes para votar",r=d();for(let e=0;e<s.length;e+=1)s[e].c();g(t,"id","voting-card-list"),g(t,"class","svelte-cbc2x7")},m(e,i){u(e,t,i),a(t,n),a(t,r);for(let e=0;e<s.length;e+=1)s[e].m(t,null);o=!0},p(e,[n]){if(1&n){const r=e[0];B(),s=D(s,n,p,1,e,r,i,t,q,mt,null,pt),L()}},i(e){if(!o){for(let e=0;e<f.length;e+=1)I(s[e]);o=!0}},o(e){for(let e=0;e<s.length;e+=1)M(s[e]);o=!1},d(e){e&&c(t);for(let e=0;e<s.length;e+=1)s[e].d()}}}function gt(e,t,n){let{movies:r}=t;return e.$$set=e=>{"movies"in e&&n(0,r=e.movies)},[r]}class vt extends J{constructor(e){super(),W(this,e,gt,ht,s,{movies:0})}}function yt(t){let n,r,o,s,i,p,m=t[0].title+"",h=t[0].stream+"";return{c(){n=l("div"),r=l("span"),o=f(m),s=d(),i=l("span"),p=f(h),g(r,"class","title svelte-15jz1k"),g(i,"class","stream svelte-15jz1k"),g(n,"class","card svelte-15jz1k")},m(e,t){u(e,n,t),a(n,r),a(r,o),a(n,s),a(n,i),a(i,p)},p(e,[t]){1&t&&m!==(m=e[0].title+"")&&v(o,m),1&t&&h!==(h=e[0].stream+"")&&v(p,h)},i:e,o:e,d(e){e&&c(n)}}}function wt(e,t,n){let{movie:r}=t;return e.$$set=e=>{"movie"in e&&n(0,r=e.movie)},[r]}class $t extends J{constructor(e){super(),W(this,e,wt,yt,s,{movie:0})}}function bt(e,t,n){const r=e.slice();return r[1]=t[n],r}function xt(e,t){let n,r,o;return r=new $t({props:{movie:t[1]}}),{key:e,first:null,c(){n=p(),F(r.$$.fragment),this.first=n},m(e,t){u(e,n,t),z(r,e,t),o=!0},p(e,t){const n={};1&t&&(n.movie=e[1]),r.$set(n)},i(e){o||(I(r.$$.fragment,e),o=!0)},o(e){M(r.$$.fragment,e),o=!1},d(e){e&&c(n),H(r,e)}}}function Et(e){let t,n,r,o,s=[],i=new Map,f=e[0].sort(_t);const p=e=>e[1].title;for(let t=0;t<f.length;t+=1){let n=bt(e,f,t),r=p(n);i.set(r,s[t]=xt(r,n))}return{c(){t=l("div"),n=l("h2"),n.textContent="Filmes para assistir",r=d();for(let e=0;e<s.length;e+=1)s[e].c();g(t,"id","voting-card-list"),g(t,"class","svelte-cbc2x7")},m(e,i){u(e,t,i),a(t,n),a(t,r);for(let e=0;e<s.length;e+=1)s[e].m(t,null);o=!0},p(e,[n]){if(1&n){const r=e[0].sort(_t);B(),s=D(s,n,p,1,e,r,i,t,q,xt,null,bt),L()}},i(e){if(!o){for(let e=0;e<f.length;e+=1)I(s[e]);o=!0}},o(e){for(let e=0;e<s.length;e+=1)M(s[e]);o=!1},d(e){e&&c(t);for(let e=0;e<s.length;e+=1)s[e].d()}}}const _t=(e,t)=>e.watchOrder-t.watchOrder;function Ct(e,t,n){let{movies:r}=t;return e.$$set=e=>{"movies"in e&&n(0,r=e.movies)},[r]}class St extends J{constructor(e){super(),W(this,e,Ct,Et,s,{movies:0})}}function Ot(t){let n;return{c(){n=f("Carregando round ...")},m(e,t){u(e,n,t)},p:e,i:e,o:e,d(e){e&&c(n)}}}function kt(e){let t,n,r,o;const s=[Rt,jt,At],i=[];function a(e,t){return"Recommendation"===e[1].step||e[0]?0:"Voting"===e[1].step?1:2}return t=a(e),n=i[t]=s[t](e),{c(){n.c(),r=p()},m(e,n){i[t].m(e,n),u(e,r,n),o=!0},p(e,o){let u=t;t=a(e),t===u?i[t].p(e,o):(B(),M(i[u],1,1,(()=>{i[u]=null})),L(),n=i[t],n?n.p(e,o):(n=i[t]=s[t](e),n.c()),I(n,1),n.m(r.parentNode,r))},i(e){o||(I(n),o=!0)},o(e){M(n),o=!1},d(e){i[t].d(e),e&&c(r)}}}function At(e){let t,n;return t=new St({props:{movies:e[1].movies}}),{c(){F(t.$$.fragment)},m(e,r){z(t,e,r),n=!0},p(e,n){const r={};2&n&&(r.movies=e[1].movies),t.$set(r)},i(e){n||(I(t.$$.fragment,e),n=!0)},o(e){M(t.$$.fragment,e),n=!1},d(e){H(t,e)}}}function jt(e){let t,n;return t=new vt({props:{movies:e[1].movies}}),{c(){F(t.$$.fragment)},m(e,r){z(t,e,r),n=!0},p(e,n){const r={};2&n&&(r.movies=e[1].movies),t.$set(r)},i(e){n||(I(t.$$.fragment,e),n=!0)},o(e){M(t.$$.fragment,e),n=!1},d(e){H(t,e)}}}function Rt(e){let t,n;return t=new ut({props:{userMovie:e[1].movies?.find(e[3]),tooManyPeopleAlreadyWatched:e[0]}}),{c(){F(t.$$.fragment)},m(e,r){z(t,e,r),n=!0},p(e,n){const r={};6&n&&(r.userMovie=e[1].movies?.find(e[3])),1&n&&(r.tooManyPeopleAlreadyWatched=e[0]),t.$set(r)},i(e){n||(I(t.$$.fragment,e),n=!0)},o(e){M(t.$$.fragment,e),n=!1},d(e){H(t,e)}}}function Nt(e){let t,n,r,o;const s=[kt,Ot],i=[];function a(e,t){return e[1]?0:1}return t=a(e),n=i[t]=s[t](e),{c(){n.c(),r=p()},m(e,n){i[t].m(e,n),u(e,r,n),o=!0},p(e,[o]){let u=t;t=a(e),t===u?i[t].p(e,o):(B(),M(i[u],1,1,(()=>{i[u]=null})),L(),n=i[t],n?n.p(e,o):(n=i[t]=s[t](e),n.c()),I(n,1),n.m(r.parentNode,r))},i(e){o||(I(n),o=!0)},o(e){M(n),o=!1},d(e){i[t].d(e),e&&c(r)}}}function Ut(e,t,n){let r,o;i(e,Ge,(e=>n(1,r=e))),i(e,Me,(e=>n(2,o=e)));let s;return e.$$.update=()=>{6&e.$$.dirty&&n(0,s="Voting"===r?.step&&r?.movies?.find((e=>e.userId===o))?.tooManyPeopleAlreadySaw)},[s,r,o,e=>e.userId===o]}class Tt extends J{constructor(e){super(),W(this,e,Ut,Nt,s,{})}}function Pt(t){let n,r;return n=new Je({}),n.$on("loggedIn",t[1]),{c(){F(n.$$.fragment)},m(e,t){z(n,e,t),r=!0},p:e,i(e){r||(I(n.$$.fragment,e),r=!0)},o(e){M(n.$$.fragment,e),r=!1},d(e){H(n,e)}}}function Bt(t){let n,r,o,s,i,a;return o=new Tt({}),{c(){n=l("button"),n.textContent="Logout",r=d(),F(o.$$.fragment)},m(e,t){u(e,n,t),u(e,r,t),z(o,e,t),s=!0,i||(a=m(n,"click",Me.logout),i=!0)},p:e,i(e){s||(I(o.$$.fragment,e),s=!0)},o(e){M(o.$$.fragment,e),s=!1},d(e){e&&c(n),e&&c(r),H(o,e),i=!1,a()}}}function Lt(e){let t,n,r,o;const s=[Bt,Pt],i=[];function a(e,t){return e[0]?0:1}return t=a(e),n=i[t]=s[t](e),{c(){n.c(),r=p()},m(e,n){i[t].m(e,n),u(e,r,n),o=!0},p(e,[o]){let u=t;t=a(e),t===u?i[t].p(e,o):(B(),M(i[u],1,1,(()=>{i[u]=null})),L(),n=i[t],n?n.p(e,o):(n=i[t]=s[t](e),n.c()),I(n,1),n.m(r.parentNode,r))},i(e){o||(I(n),o=!0)},o(e){M(n),o=!1},d(e){i[t].d(e),e&&c(r)}}}function It(e,t,n){let r;i(e,Me,(e=>n(0,r=e)));return[r,e=>Me.update(e.detail)]}return new class extends J{constructor(e){super(),W(this,e,It,Lt,s,{})}}({target:document.body})}();
//# sourceMappingURL=bundle.js.map

import{d as U,r as v,F as j,n as G,o as u,c as y,b as a,w as r,e as l,f as i,G as B,H as D,I as H,g as J,E as n,h as c,J as K,q as P,t as d,u as h,K as R,A as O,y as Q,_ as W}from"./index-BekgHCmB.js";import{g as X,a as Y,b as Z}from"./product-BOdXUwmS.js";import{a as ee}from"./cart-CBZAfa9Q.js";import{a as te}from"./favorite-Cnk43VDe.js";import"./user-DuJHeInz.js";const oe={class:"product-container"},se={class:"header"},ae={class:"category-filter"},re={class:"product-list"},ne={class:"product-content"},le=["onClick"],ce={class:"image-slot"},ie={class:"product-info"},de={class:"product-meta"},ue={class:"product-footer"},_e={class:"product-actions"},pe=U({__name:"products",setup(fe){const T=J(),f=v([]),C=v([]),_=v(null),m=v(!0),F=async()=>{try{const e=(await X()).data;e.code===200?C.value=e.data:n.error(e.message||"获取商品分类失败")}catch{n.error("获取商品分类失败")}},I=j(()=>_.value?f.value.filter(o=>o.categoryId===_.value):f.value),k=async()=>{try{const e=(await Y()).data;e.code===200?f.value=e.data:n.error(e.message||"获取商品列表失败")}catch{n.error("获取商品列表失败")}finally{m.value=!1}},$=async o=>{m.value=!0,_.value=o;try{if(o===null)await k();else{const s=(await Z(o)).data;s.code===200?f.value=s.data:n.error(s.message||"获取商品列表失败")}}catch{n.error("获取商品列表失败")}finally{m.value=!1}},z=async o=>{try{const e={productId:o.id,quantity:1},s=await ee(e);if(s.data){const p=s.data;p.code===200?n.success("添加到购物车成功"):n.error(p.message||"添加到购物车失败")}else n.error("添加到购物车失败")}catch{n.error("添加到购物车失败")}},A=async o=>{if(!localStorage.getItem("token")){n.warning("请先登录");return}try{console.log("开始添加收藏，商品ID:",o.id);const s=await te(o.id);console.log("收藏响应:",s),s.data.code===200&&n.success("收藏成功")}catch(s){console.error("收藏失败:",s),s.response&&console.error("错误响应:",s.response)}},E=o=>{T.push(`/product/${o.id}`)};return G(()=>{k(),F()}),(o,e)=>{const s=c("el-radio-button"),p=c("el-radio-group"),w=c("el-card"),N=c("el-icon"),S=c("el-image"),g=c("el-text"),b=c("el-tag"),x=c("el-button"),q=c("el-col"),L=c("el-row"),M=K("loading");return u(),y("div",oe,[a(w,{class:"page-header"},{header:r(()=>[l("div",se,[e[2]||(e[2]=l("span",{class:"title"},"商品列表",-1)),l("div",ae,[a(p,{modelValue:_.value,"onUpdate:modelValue":e[0]||(e[0]=t=>_.value=t),onChange:$},{default:r(()=>[a(s,{label:null},{default:r(()=>e[1]||(e[1]=[i("全部")])),_:1}),(u(!0),y(B,null,D(C.value,t=>(u(),P(s,{key:t.id,label:t.id},{default:r(()=>[i(d(t.name),1)]),_:2},1032,["label"]))),128))]),_:1},8,["modelValue"])])])]),_:1}),H((u(),y("div",re,[a(L,{gutter:20},{default:r(()=>[(u(!0),y(B,null,D(I.value,t=>(u(),P(q,{xs:24,sm:12,md:8,lg:6,key:t.id},{default:r(()=>[a(w,{class:"product-card","body-style":{padding:"0px"}},{default:r(()=>[l("div",ne,[l("div",{class:"product-main",onClick:V=>E(t)},[a(S,{src:t.imageUrl||"/default-product.jpg",alt:t.name,class:"product-image",fit:"cover"},{error:r(()=>[l("div",ce,[a(N,null,{default:r(()=>[a(h(R))]),_:1})])]),_:2},1032,["src","alt"]),l("div",ie,[a(g,{class:"product-name",truncated:""},{default:r(()=>[i(d(t.name),1)]),_:2},1024),a(g,{class:"product-description",type:"info"},{default:r(()=>[i(d(t.description),1)]),_:2},1024),l("div",de,[a(b,{size:"small",type:"info"},{default:r(()=>[i(d(t.brand),1)]),_:2},1024),a(b,{size:"small",type:"info"},{default:r(()=>[i(d(t.model),1)]),_:2},1024)]),l("div",ue,[a(g,{class:"price",type:"danger",size:"large"},{default:r(()=>[i("¥"+d(t.price.toLocaleString()),1)]),_:2},1024),a(g,{class:"stock",type:"info"},{default:r(()=>[i("库存: "+d(t.stock),1)]),_:2},1024)])])],8,le),l("div",_e,[a(x,{type:"warning",icon:h(O),circle:"",onClick:V=>A(t)},null,8,["icon","onClick"]),a(x,{type:"primary",icon:h(Q),circle:"",onClick:V=>z(t),disabled:t.stock<=0},null,8,["icon","onClick","disabled"])])])]),_:2},1024)]),_:2},1024))),128))]),_:1})])),[[M,m.value]])])}}}),Ce=W(pe,[["__scopeId","data-v-820d7025"]]);export{Ce as default};

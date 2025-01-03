import{d as $,r as w,a as F,j as P,o as c,q as h,w as o,e as t,b as n,f as I,u as q,E as m,h as u,P as j,_ as z,n as E,c as k,I as T,G as S,t as i,H as G,M as U,s as H,J}from"./index-D07GUNLX.js";import{b as W}from"./order-D29R81T9.js";import{a as K}from"./review-DIY5lUzj.js";import{f as Q}from"./date-D_0JMZE-.js";import"./user-B44x33Ff.js";const X={class:"dialog-footer"},Y=$({__name:"ReviewForm",props:{modelValue:{type:Boolean},orderId:{},productId:{}},emits:["success","update:modelValue"],setup(O,{emit:R}){const a=O,b=R,v=w(a.modelValue),g=w(!1),V=w(),l=w([]),p=F({rating:5,content:""}),C={rating:[{required:!0,message:"请选择评分",trigger:"change"}],content:[{required:!0,message:"请输入评价内容",trigger:"blur"},{min:5,max:500,message:"评价内容长度应在5-500个字符之间",trigger:"blur"}]},D=e=>{var f;if(l.value.length>6){m.warning("最多只能上传6张图片"),l.value=l.value.slice(0,6);return}if(!((f=e.raw)==null?void 0:f.type.startsWith("image/"))){m.error("只能上传图片文件");const _=l.value.indexOf(e);_!==-1&&l.value.splice(_,1);return}if(!(e.size/1024/1024<5)){m.error("图片大小不能超过5MB");const _=l.value.indexOf(e);_!==-1&&l.value.splice(_,1)}},M=e=>{const s=l.value.indexOf(e);s!==-1&&l.value.splice(s,1)},r=async()=>{V.value&&await V.value.validate(async e=>{if(e){g.value=!0;try{const s=await K(a.orderId,a.productId,{rating:p.rating,content:p.content,files:l.value.map(x=>x.raw)});s.data.code===200?(m.success("评价提交成功"),v.value=!1,b("success"),p.rating=5,p.content="",l.value=[]):m.error(s.data.message||"评价提交失败")}catch(s){m.error(s.message||"评价提交失败")}finally{g.value=!1}}})};return P(()=>a.modelValue,e=>{v.value=e}),P(v,e=>{b("update:modelValue",e)}),(e,s)=>{const x=u("el-rate"),f=u("el-form-item"),_=u("el-input"),B=u("el-icon"),N=u("el-upload"),d=u("el-form"),L=u("el-button"),A=u("el-dialog");return c(),h(A,{modelValue:v.value,"onUpdate:modelValue":s[4]||(s[4]=y=>v.value=y),title:"发表评价",width:"600px","close-on-click-modal":!1},{footer:o(()=>[t("span",X,[n(L,{onClick:s[3]||(s[3]=y=>v.value=!1)},{default:o(()=>s[6]||(s[6]=[I("取消")])),_:1}),n(L,{type:"primary",loading:g.value,onClick:r},{default:o(()=>s[7]||(s[7]=[I(" 提交评价 ")])),_:1},8,["loading"])])]),default:o(()=>[n(d,{ref_key:"formRef",ref:V,model:p,rules:C,"label-width":"80px",class:"review-form"},{default:o(()=>[n(f,{label:"评分",prop:"rating"},{default:o(()=>[n(x,{modelValue:p.rating,"onUpdate:modelValue":s[0]||(s[0]=y=>p.rating=y),texts:["很差","较差","一般","不错","很好"],"show-text":""},null,8,["modelValue"])]),_:1}),n(f,{label:"评价内容",prop:"content"},{default:o(()=>[n(_,{modelValue:p.content,"onUpdate:modelValue":s[1]||(s[1]=y=>p.content=y),type:"textarea",rows:4,placeholder:"请输入您的评价内容"},null,8,["modelValue"])]),_:1}),n(f,{label:"上传图片"},{default:o(()=>[n(N,{"file-list":l.value,"onUpdate:fileList":s[2]||(s[2]=y=>l.value=y),action:"#","list-type":"picture-card","auto-upload":!1,"on-change":D,"on-remove":M},{tip:o(()=>s[5]||(s[5]=[t("div",{class:"upload-tip"}," 支持jpg/png文件，最多可上传6张图片 ",-1)])),default:o(()=>[n(B,null,{default:o(()=>[n(q(j))]),_:1})]),_:1},8,["file-list"])]),_:1})]),_:1},8,["model"])]),_:1},8,["modelValue"])}}}),Z=z(Y,[["__scopeId","data-v-a4df758a"]]),ee={class:"order-detail-container"},te={class:"section-header"},se={class:"info-grid"},ae={class:"info-item"},oe={class:"value"},le={class:"info-item"},ne={class:"value"},re={class:"info-item"},ie={class:"value"},de={class:"info-grid"},ue={class:"info-item"},ce={class:"value"},pe={class:"info-item"},me={class:"value"},ve={class:"info-item"},fe={class:"value"},_e={class:"products-list"},ge={class:"product-details"},ye={class:"product-name"},we={class:"product-price"},be={class:"subtotal"},Ve={class:"product-actions"},xe={class:"order-amount"},ke={class:"amount-item"},he={key:0,class:"amount-item discount"},Ie={class:"amount-item final"},Re=$({__name:"order-detail",setup(O){const R=H(),a=w(null),b=w(!0),v=w(!1),g=w(null),V=async()=>{const r=Number(R.params.id);if(!r){m.error("订单ID无效");return}try{b.value=!0;const e=await W(r);e.data.code===200?a.value=e.data.data:e.data.code===500?m.error("服务器内部错误"):m.error(e.data.message||"获取订单详情失败")}catch(e){console.error("获取订单详情失败:",e),m.error("网络错误，请稍后重试")}finally{b.value=!1}},l=r=>r==null?"¥0.00":`¥${r.toFixed(2).replace(/\B(?=(\d{3})+(?!\d))/g,",")}`,p=r=>({0:"待付款",1:"待发货",2:"已发货",3:"已完成",4:"已取消"})[r]||"未知状态",C=r=>{a.value&&(g.value={orderId:a.value.id,productId:r},v.value=!0)},D=()=>{m.success("评价成功"),V()},M=r=>a.value?(console.log("订单状态:",a.value.status),console.log("商品评价状态:",r.hasReviewed),a.value.status===3&&!r.hasReviewed):!1;return E(()=>{V()}),(r,e)=>{const s=u("el-card"),x=u("el-empty"),f=u("el-tag"),_=u("el-image"),B=u("el-button"),N=J("loading");return c(),k("div",ee,[n(s,{class:"page-header"},{header:o(()=>e[1]||(e[1]=[t("div",{class:"header"},[t("span",{class:"title"},"订单详情")],-1)])),_:1}),T((c(),k("div",null,[a.value?(c(),k(S,{key:1},[n(s,{class:"order-info"},{header:o(()=>[t("div",te,[e[2]||(e[2]=t("span",{class:"section-title"},"订单信息",-1)),n(f,{type:a.value.status===4?"info":a.value.status===0?"warning":"success"},{default:o(()=>[I(i(p(a.value.status)),1)]),_:1},8,["type"])])]),default:o(()=>[t("div",se,[t("div",ae,[e[3]||(e[3]=t("span",{class:"label"},"订单编号：",-1)),t("span",oe,i(a.value.id),1)]),t("div",le,[e[4]||(e[4]=t("span",{class:"label"},"创建时间：",-1)),t("span",ne,i(q(Q)(a.value.createTime)),1)]),t("div",re,[e[5]||(e[5]=t("span",{class:"label"},"备注：",-1)),t("span",ie,i(a.value.remark||"无"),1)])])]),_:1}),n(s,{class:"delivery-info"},{header:o(()=>e[6]||(e[6]=[t("div",{class:"section-header"},[t("span",{class:"section-title"},"收货信息")],-1)])),default:o(()=>[t("div",de,[t("div",ue,[e[7]||(e[7]=t("span",{class:"label"},"收货人：",-1)),t("span",ce,i(a.value.receiverName),1)]),t("div",pe,[e[8]||(e[8]=t("span",{class:"label"},"联系电话：",-1)),t("span",me,i(a.value.receiverPhone),1)]),t("div",ve,[e[9]||(e[9]=t("span",{class:"label"},"收货地址：",-1)),t("span",fe,i(a.value.address),1)])])]),_:1}),n(s,{class:"products-info"},{header:o(()=>e[10]||(e[10]=[t("div",{class:"section-header"},[t("span",{class:"section-title"},"商品信息")],-1)])),default:o(()=>[t("div",_e,[(c(!0),k(S,null,G(a.value.items,d=>(c(),k("div",{key:d.id,class:"product-item"},[n(_,{src:d.productImage||"/default-product.jpg",fit:"cover",class:"product-image"},null,8,["src"]),t("div",ge,[t("h3",ye,i(d.productName),1),t("div",we,[t("span",null,i(l(d.productPrice))+" × "+i(d.quantity),1),t("span",be,"小计："+i(l(d.subtotal)),1)]),t("div",Ve,[M(d)?(c(),h(B,{key:0,type:"primary",size:"small",onClick:L=>C(d.productId)},{default:o(()=>e[11]||(e[11]=[I(" 评价商品 ")])),_:2},1032,["onClick"])):d.hasReviewed?(c(),h(f,{key:1,type:"info",size:"small"},{default:o(()=>e[12]||(e[12]=[I(" 已评价 ")])),_:1})):(c(),h(f,{key:2,type:"warning",size:"small"},{default:o(()=>[I(i(a.value.status===3?"未评价":"待完成"),1)]),_:1}))])])]))),128))]),t("div",xe,[t("div",ke,[e[13]||(e[13]=t("span",null,"商品总额：",-1)),t("span",null,i(l(a.value.totalAmount)),1)]),a.value.discountAmount>0?(c(),k("div",he,[e[14]||(e[14]=t("span",null,"优惠金额：",-1)),t("span",null,"-"+i(l(a.value.discountAmount)),1)])):U("",!0),t("div",Ie,[e[15]||(e[15]=t("span",null,"实付金额：",-1)),t("span",null,i(l(a.value.payAmount)),1)])])]),_:1})],64)):(c(),h(x,{key:0,description:"订单不存在"}))])),[[N,b.value]]),g.value?(c(),h(Z,{key:0,modelValue:v.value,"onUpdate:modelValue":e[0]||(e[0]=d=>v.value=d),"order-id":g.value.orderId,"product-id":g.value.productId,onSuccess:D},null,8,["modelValue","order-id","product-id"])):U("",!0)])}}}),Le=z(Re,[["__scopeId","data-v-23e0d675"]]);export{Le as default};

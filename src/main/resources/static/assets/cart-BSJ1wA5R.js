import{g as oe,u as le,r as ne,b as re}from"./cart-CBZAfa9Q.js";import{c as de}from"./order-D64j-J59.js";import{d as ie,r as f,F as ce,n as ue,o as r,c as m,b as l,w as a,e as o,I as q,q as h,G as w,H as E,f as v,t as _,L as O,u as k,g as pe,E as n,h as c,J as fe,N as me,O as _e,P as ve,C as ye,B as ge,Q as he}from"./index-BekgHCmB.js";import{a as ke}from"./address-yuD6gt1d.js";import{g as xe}from"./coupon-MfualckF.js";import"./user-DuJHeInz.js";const we={class:"cart-container"},be={class:"cart-content"},Ve={class:"item-content"},Ce={class:"item-info"},Ie={class:"item-name"},Ue={class:"item-price"},$e={class:"item-actions"},Ae={class:"footer-content"},Be={class:"total-price"},Le={class:"price"},ze={class:"address-form"},De={class:"address-info"},Ne={class:"address-header"},Se={class:"receiver"},Pe={class:"phone"},qe={class:"address-content"},Ee={class:"address-actions"},Oe={style:{"max-height":"300px","overflow-y":"auto"}},Te={style:{padding:"8px 0"}},Fe={style:{display:"flex","justify-content":"space-between","align-items":"center",width:"100%"}},Me={style:{display:"flex","align-items":"center",gap:"12px"}},Qe={style:{color:"var(--el-color-danger)","font-size":"16px","font-weight":"bold","min-width":"80px"}},je={style:{display:"flex","flex-direction":"column",gap:"4px"}},Re={style:{color:"var(--el-text-color-secondary)","font-size":"12px"}},Ge={style:{color:"var(--el-text-color-secondary)","font-size":"12px"}},He={class:"dialog-footer"},tt=ie({__name:"cart",setup(Je){const b=pe(),u=f([]),B=f(!0),T=f(null),y=f(0),U=f(""),V=f(!1),x=f([]),L=f(!1),C=f(0),$=f([]),z=f(!1),F=ce(()=>u.value.filter(s=>s.selected===1).reduce((s,e)=>s+(e.product.productPrice||0)*e.quantity,0)),M=s=>s?s.toLocaleString():"0",R=async()=>{var s;try{B.value=!0;const d=(await oe()).data;d.code===200&&(u.value=d.data||[])}catch(e){((s=e.response)==null?void 0:s.status)===401?n.error("请先登录"):n.error(e.message||"获取购物车列表失败")}finally{B.value=!1}},G=async(s,e)=>{if(!(e<1))try{const i=(await le(s.id,e)).data;i.code===200?(s.quantity=e,n.success("更新数量成功")):n.error(i.message||"更新数量失败")}catch{n.error("更新数量失败")}},H=async s=>{try{await he.confirm("确定要删除这个商品吗？","提示",{confirmButtonText:"确定",cancelButtonText:"取消",type:"warning"}),T.value=s.id;const e=await ne(s.productId);if(e.data){const d=e.data;d.code===200?(u.value=u.value.filter(i=>i.id!==s.id),n.success("删除成功")):n.error(d.message||"删除失败")}else n.error("删除失败")}catch(e){e!=="cancel"&&n.error("删除失败")}finally{T.value=null}},J=async s=>{try{const e=s.selected===1?0:1,i=(await re(s.id,e)).data;i.code===200?s.selected=e:n.error(i.message||"更新选中状态失败")}catch{n.error("更新选中状态失败")}},K=async()=>{try{L.value=!0;const s=await ke.getAddressList();if(s.data.code===200){x.value=s.data.data;const e=x.value.find(d=>d.isDefault);e?y.value=e.id:x.value.length>0&&(y.value=x.value[0].id)}}catch{n.error("获取地址列表失败")}finally{L.value=!1}},W=async()=>{try{z.value=!0;const s=await xe();s.data.code===200&&($.value=s.data.data.filter(e=>e.status===0))}catch{n.error("获取优惠券列表失败")}finally{z.value=!1}},X=()=>{if(u.value.filter(e=>e.selected===1).length===0){n.warning("请选择要结算的商品");return}V.value=!0,K(),W()},Y=async()=>{var s,e,d;if(!y.value){n.warning("请选择收货地址");return}try{const D={cartIds:u.value.filter(p=>p.selected===1).map(p=>p.id),addressId:y.value,remark:U.value||"",userCouponId:C.value||void 0};if(!localStorage.getItem("token")){n.error("请先登录"),b.push("/login");return}const I=await de(D);I.data.code===200?(u.value=u.value.filter(p=>p.selected!==1),n.success("订单创建成功"),V.value=!1,b.push(`/order/${I.data.data.id}`)):n.error(I.data.message||"创建订单失败")}catch(i){console.error("创建订单失败:",i),((s=i.response)==null?void 0:s.status)===401?(n.error("请先登录"),b.push("/login")):n.error(((d=(e=i.response)==null?void 0:e.data)==null?void 0:d.message)||"创建订单失败")}};return ue(()=>{R()}),(s,e)=>{const d=c("el-card"),i=c("el-empty"),D=c("el-checkbox"),A=c("el-icon"),I=c("el-input-number"),p=c("el-button"),Z=c("el-tag"),N=c("el-radio"),Q=c("el-radio-group"),ee=c("el-link"),S=c("el-form-item"),j=c("el-input"),te=c("el-popover"),se=c("el-form"),ae=c("el-dialog"),P=fe("loading");return r(),m("div",we,[l(d,{class:"page-header"},{header:a(()=>e[7]||(e[7]=[o("div",{class:"header"},[o("span",{class:"title"},"购物车")],-1)])),_:1}),q((r(),m("div",be,[u.value.length===0?(r(),h(i,{key:0,description:"购物车是空的"})):(r(),m(w,{key:1},[(r(!0),m(w,null,E(u.value,t=>(r(),h(d,{key:t.id,class:"cart-item"},{default:a(()=>[o("div",Ve,[l(D,{modelValue:t.selected,"onUpdate:modelValue":g=>t.selected=g,"true-label":1,"false-label":0,onChange:()=>J(t)},null,8,["modelValue","onUpdate:modelValue","onChange"]),o("div",Ce,[o("h3",Ie,_(t.product.productName),1),o("div",Ue,"¥"+_(M(t.product.productPrice)),1)]),o("div",$e,[l(I,{modelValue:t.quantity,"onUpdate:modelValue":g=>t.quantity=g,min:1,max:99,size:"small",onChange:g=>G(t,g)},{decrease:a(()=>[l(A,null,{default:a(()=>[l(k(me))]),_:1})]),increase:a(()=>[l(A,null,{default:a(()=>[l(k(_e))]),_:1})]),_:2},1032,["modelValue","onUpdate:modelValue","onChange"]),l(p,{type:"danger",icon:k(ve),circle:"",onClick:g=>H(t)},null,8,["icon","onClick"])])])]),_:2},1024))),128)),u.value.length>0?(r(),h(d,{key:0,class:"cart-footer"},{default:a(()=>[o("div",Ae,[o("div",Be,[e[8]||(e[8]=v(" 总计: ")),o("span",Le,"¥"+_(M(F.value)),1)]),l(p,{type:"primary",size:"large",onClick:X,disabled:!u.value.some(t=>t.selected===1)},{default:a(()=>e[9]||(e[9]=[v(" 结算 ")])),_:1},8,["disabled"])])]),_:1})):O("",!0)],64))])),[[P,B.value]]),l(ae,{modelValue:V.value,"onUpdate:modelValue":e[6]||(e[6]=t=>V.value=t),title:"选择收货地址",width:"50%","close-on-click-modal":!1},{footer:a(()=>[o("span",He,[l(p,{onClick:e[5]||(e[5]=t=>V.value=!1)},{default:a(()=>e[14]||(e[14]=[v("取消")])),_:1}),l(p,{type:"primary",onClick:Y,disabled:!y.value},{default:a(()=>e[15]||(e[15]=[v(" 提交订单 ")])),_:1},8,["disabled"])])]),default:a(()=>[q((r(),m("div",ze,[l(se,{model:{addressId:y.value,remark:U.value},"label-width":"100px"},{default:a(()=>[l(S,{label:"收货地址",required:""},{default:a(()=>[x.value.length===0?(r(),h(i,{key:0,description:"暂无收货地址"},{default:a(()=>[l(p,{type:"primary",onClick:e[0]||(e[0]=t=>k(b).push("/address"))},{default:a(()=>e[10]||(e[10]=[v(" 添加收货地址 ")])),_:1})]),_:1})):(r(),h(Q,{key:1,modelValue:y.value,"onUpdate:modelValue":e[1]||(e[1]=t=>y.value=t),class:"address-list"},{default:a(()=>[(r(!0),m(w,null,E(x.value,t=>(r(),h(N,{key:t.id,label:t.id,class:"address-item"},{default:a(()=>[o("div",De,[o("div",Ne,[o("span",Se,_(t.receiverName),1),o("span",Pe,_(t.receiverPhone),1),t.isDefault?(r(),h(Z,{key:0,size:"small",type:"success"},{default:a(()=>e[11]||(e[11]=[v("默认")])),_:1})):O("",!0)]),o("div",qe,_(`${t.province}${t.city}${t.district}${t.detailAddress}`),1)])]),_:2},1032,["label"]))),128))]),_:1},8,["modelValue"])),o("div",Ee,[l(ee,{type:"primary",onClick:e[2]||(e[2]=t=>k(b).push("/address"))},{default:a(()=>e[12]||(e[12]=[v(" 管理收货地址 ")])),_:1})])]),_:1}),l(S,{label:"订单备注"},{default:a(()=>[l(j,{modelValue:U.value,"onUpdate:modelValue":e[3]||(e[3]=t=>U.value=t),type:"textarea",rows:3,placeholder:"请输入订单备注（选填）"},null,8,["modelValue"])]),_:1}),l(S,{label:"优惠券"},{default:a(()=>[q((r(),m("div",null,[l(te,{placement:"bottom-start",width:400,trigger:"click","popper-class":"coupon-popover"},{reference:a(()=>{var t;return[l(j,{readonly:"",placeholder:C.value===0?"不使用优惠券":((t=$.value.find(g=>g.id===C.value))==null?void 0:t.coupon.name)||"请选择优惠券","suffix-icon":k(ye),style:{width:"100%"}},null,8,["placeholder","suffix-icon"])]}),default:a(()=>[o("div",Oe,[l(Q,{modelValue:C.value,"onUpdate:modelValue":e[4]||(e[4]=t=>C.value=t),style:{width:"100%"}},{default:a(()=>[o("div",Te,[l(N,{label:0,style:{width:"100%",margin:"0",padding:"8px 16px"}},{default:a(()=>e[13]||(e[13]=[o("div",{style:{display:"flex","justify-content":"space-between","align-items":"center",width:"100%"}},[o("span",null,"不使用优惠券"),o("span",{style:{color:"var(--el-text-color-secondary)"}},"按原价支付")],-1)])),_:1})]),(r(!0),m(w,null,E($.value,t=>(r(),m("div",{key:t.id,style:{padding:"4px 0"}},[l(N,{label:t.id,disabled:t.coupon.minAmount>F.value,style:{width:"100%",margin:"0",padding:"8px 16px"}},{default:a(()=>[o("div",Fe,[o("div",Me,[o("span",Qe,[t.coupon.type===1?(r(),m(w,{key:0},[v(" ¥"+_(t.coupon.value),1)],64)):(r(),m(w,{key:1},[v(_(t.coupon.value)+"折 ",1)],64))]),o("div",je,[o("span",null,_(t.coupon.name),1),o("span",Re," 满"+_(t.coupon.minAmount)+"元可用 ",1)])]),o("span",Ge," 有效期至："+_(t.coupon.endTime),1)])]),_:2},1032,["label","disabled"])]))),128))]),_:1},8,["modelValue"]),$.value.length===0?(r(),h(i,{key:0,description:"暂无可用优惠券"},{image:a(()=>[l(A,{size:60},{default:a(()=>[l(k(ge))]),_:1})]),_:1})):O("",!0)])]),_:1})])),[[P,z.value]])]),_:1})]),_:1},8,["model"])])),[[P,L.value]])]),_:1},8,["modelValue"])])}}});export{tt as default};
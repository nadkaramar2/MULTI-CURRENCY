// import React from "react";

// const ViewReqResponse = ({ props }) => {
//   const viewTxnid = props.indata;
//   const viewTxn = props.in1data;
//   console.log(viewTxnid, viewTxn);
//   return (
//     <>
//       <div className="mx-6">
//         <div className="w-full shadow-md mt-2">
//           <div className="px-4 py-1 sm:px-10  bg-blue-200">
//             <div className=" flex  items-center justify-between">
//               <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
//                 View Transaction Request Response
//               </p>
//             </div>
//           </div>
//         </div>
//         <div className="flex items-center rounded-md mt-1 ">
//           <div className="flex-1 max-w-5xl mx-auto p-4  ">
//             <ul className="grid grid-cols-3 gap-2">
//               <li className="bg-gray-200 text-black font-semiboild shadow rounded-md text-center p-1">
//                 <p className="">Montra Txn ID</p>

//                 <p className=" bg-blue-200 text-black  shadow rounded-md font-bold">
//                   {viewTxnid || "---"}
//                 </p>
//               </li>

//               <li className="bg-gray-200 text-black font-semiboild shadow rounded-md text-center p-1">
//                 <p className="">AMS Txn ID </p>

//                 <p className="bg-blue-200 text-black  shadow rounded-md font-bold">
//                   {/* {data2.amsTransactionId || "---"} */}
//                 </p>
//               </li>

//               <li className="bg-gray-200 text-black font-semiboild shadow rounded-md text-center p-1">
//                 <p className=""> Confirn Txn ID </p>

//                 <p className="bg-blue-200 text-black  shadow rounded-md font-bold">
//                   {viewTxn || "---"}
//                 </p>
//               </li>
//             </ul>
//           </div>
//         </div>

//         <div className="mt-4 flex justify-center items-center">
//           <button
//             type="button"
//             className="inline-flex justify-center px-4 py-2 text-lg font-bold text-white bg-blue-800 border border-transparent rounded-md"
//             onClick={props.onClose}
//           >
//             Close
//           </button>
//         </div>
//       </div>
//     </>
//   );
// };

// export default ViewReqResponse;

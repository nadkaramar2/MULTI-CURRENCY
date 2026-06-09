import React from "react";
import AppLayout from "../../layout/AppLayout";
export default function CustomerDetails() {
  //   const handleClick = () => {
  // 👇️ clear input value
  // setNum10("");
  //   };
  return (
    <AppLayout>
      <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-500  bg-gradient-to-r from-blue-700 to-sky-300 py-6 mt-4 rounded-t-lg ">
        <h1 className="font-medium  text-white text-2xl">Customer Details</h1>
      </div>
      <div className="mt-5 md:col-span-2 lg:col-span-1  bg-blue-200">
        <form className="">
          <div className="overflow-hidden shadow sm:rounded-md">
            <div className=" px-4 py-5 sm:p-6 bg-blue-200  ">
              <div className="grid gap-6 mb-6 md:grid-cols-3 ">
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    First Name*
                  </label>
                  <input
                    type="text"
                    id="GL Account Type"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="GL Account Description"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Middle Name*
                  </label>
                  <input
                    type="text"
                    id="GL Account Description"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="GL Account Type"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Last Name*
                  </label>
                  <input
                    type="text"
                    id="GL Account Type"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Gender*
                  </label>
                  <select
                    id="accountType"
                    name="accountType"
                    // value={transactionData1}
                    // onChange={(e) => settransactionData1(e.target.value)}
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    required
                  >
                    <option>Male</option>
                    <option>Female</option>
                    {/* {transactionData.map((data) => ( */}
                    {/* <optgroup kay={uuidv4()}> */}

                    {/* </optgroup> */}
                    {/* ))} */}
                  </select>
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Date Of Birth*
                  </label>
                  <input
                    type="date"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Email Id*
                  </label>
                  <input
                    type="text"
                    id="gmail"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Address Line1*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Address Line2*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Address Line3*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Country*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    State*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    City*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Mobile Number*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
                <div>
                  <label
                    htmlFor="text"
                    className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
                  >
                    Postal Code*
                  </label>
                  <input
                    type="text"
                    id="phone"
                    className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2.5 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
                    placeholder=""
                    required
                  />
                </div>
              </div>
            </div>
          </div>
        </form>
      </div>
    </AppLayout>
  );
}

// import React, { useState, useEffect, Fragment } from "react";
// import amsApi from "../../api/amsApi";
// import AppLayout from "../../layout/AppLayout";
// import { v4 as uuidv4 } from "uuid";
// import Swal from "sweetalert2";
// import { Dialog, Transition } from "@headlessui/react";
// export default function ChargingConfigu() {
//   const [accountType, setAccountType] = useState("");
//   const [alldata, setAlldata] = useState([]);
//   let participantID = sessionStorage.getItem("Participantid");
//   const [searchQuery, setSearchQuery] = useState("");
//   const [searchQuery1, setSearchQuery1] = useState("");
//   const [searchQuery2, setSearchQuery2] = useState("");
//   let userid = localStorage.getItem("userName");
//   const showSuccess = (resMessage) => {
//     Swal.fire({
//       title: "Success",
//       text: resMessage,
//       allowOutsideClick: false,
//       icon: "success",
//       confirmButtonText: "OK",
//     });
//   };
//   const showError = (resMessage) => {
//     Swal.fire({
//       text: resMessage,
//       allowOutsideClick: false,
//       icon: "error",
//       title: "Oops...",
//       confirmButtonText: "OK",
//     });
//   };

//   let [isOpen, setIsOpen] = useState(false);

//   function closeModal() {
//     setIsOpen(false);
//   }

//   function openModal() {
//     setIsOpen(true);
//   }
//   useEffect(() => {
//     AccountType();
//   }, []);

//   const AccountType = async () => {
//     try {
//       const response = await amsApi.get(`/accountType/getAccntType`, {
//         headers: {
//           "Content-Type": "application/json",
//         },
//       });
//       if (response.data.code === "S0000") {
//         setAlldata(response.data.accountTypeMasterlistData);
//       } else {
//         // showError(response.data.message);
//       }
//     } catch (error) {}
//   };

//   //   1
//   const [rows, setRows] = useState([]);

//   const cardrelated = async () => {
//     try {
//       const response = await amsApi.post(
//         `getChargeMasterList/getChargeMasterList`,
//         {}
//       );
//       if (response.data.code === "S0000") {
//         setRows(response.data.chargeMasters);
//         // showSuccess(response.data.message);
//       } else {
//         showError(response.data.message);
//       }
//     } catch (error) {
//       showError(error);
//     }
//   };

//   // for charge related
//   const handleCheckboxChange = (event, strChargeType) => {
//     const values = [...rows];
//     for (let index = 0; index < values.length; index++) {
//       values[index].isDisabled = false;

//       // values[index].amount = "-";

//       setRows(values);
//     }
//     const index = values.findIndex(
//       (row) => row.strChargeType === strChargeType
//     );
//     values[index].isDisabled = event.target.checked;
//     setRows(values);
//     console.log(values);
//   };

//   const handleInputChange = (event, strChargeType) => {
//     const values = [...rows];
//     const index = values.findIndex(
//       (row) => row.strChargeType === strChargeType
//     );
//     values[index][event.target.strChargeType] = event.target.value;

//     setRows(values);
//   };

//   // Transaction Related
//   const [list1, setList1] = useState([]);
//   const TransactionCharg = async () => {
//     try {
//       const response = await amsApi.post(
//         `getChargeMasterList/getTransactionChargList`,
//         {}
//       );
//       if (response.data.code === "S0000") {
//         setList1(response.data.chargeMasters);
//         // showSuccess(response.data.message);
//       } else {
//         showError(response.data.message);
//       }
//     } catch (error) {
//       showError(error);
//     }
//   };

//   const handleCheckboxChange1 = (event, strChargeType) => {
//     const values = [...list1];
//     for (let index = 0; index < values.length; index++) {
//       values[index].isDisabled = false;

//       // values[index].amount = "-";

//       setList1(values);
//     }
//     const index = values.findIndex(
//       (data) => data.strChargeType === strChargeType
//     );
//     values[index].isDisabled = event.target.checked;
//     setList1(values);
//   };

//   const handleInputChange1 = (event, strChargeType) => {
//     const values = [...list1];
//     const index = values.findIndex(
//       (data) => data.strChargeType === strChargeType
//     );
//     values[index][event.target.strChargeType] = event.target.value;
//     setList1(values);
//   };

//   // Fuel charge
//   const [list2, setList2] = useState([]);
//   const FuelCharg = async () => {
//     try {
//       const response = await amsApi.post(
//         `getChargeMasterList/getFuelChargList`,
//         {}
//       );
//       if (response.data.code === "S0000") {
//         setList2(response.data.chargeMasters);
//         // showSuccess(response.data.message);
//       } else {
//         showError(response.data.message);
//       }
//     } catch (error) {
//       showError(error);
//     }
//   };

//   const handleCheckboxChange2 = (event, strChargeType) => {
//     const values = [...list2];
//     for (let index = 0; index < values.length; index++) {
//       values[index].isDisabled = false;

//       // values[index].amount = "-";

//       setList2(values);
//     }

//     const index = values.findIndex(
//       (value) => value.strChargeType === strChargeType
//     );
//     values[index].isDisabled = event.target.checked;
//     setList2(values);
//   };

//   const handleInputChange2 = (event, strChargeType) => {
//     const values = [...list1];
//     const index = values.findIndex(
//       (value) => value.strChargeType === strChargeType
//     );
//     values[index][event.target.strChargeType] = event.target.value;
//     setList1(values);
//   };

//   // save button

//   const handleSubmit = async (event) => {
//     event.preventDefault();
//     let arr = [];

//     for (let index = 0; index < rows.length; index++) {
//       if (rows[index].isDisabled === true) {
//         // console.log(rows[index]);

//         arr.push(rows[index]);
//       }
//     }

//     for (let index = 0; index < list1.length; index++) {
//       if (list1[index].isDisabled === true) {
//         // console.log(rows[index]);

//         arr.push(list1[index]);
//       }
//     }

//     for (let index = 0; index < list2.length; index++) {
//       if (list2[index].isDisabled === true) {
//         // console.log(rows[index]);

//         arr.push(list2[index]);
//       }
//     }

//     console.log(arr);

//     // if (selectedData.length !== 0) {
//     //   if (selectedData[i].isDisabled === true) {
//     //     alert(selectedData[i]);
//     //   }

//     // }

//     // console.log(`{"data":` + JSON.stringify(selectedData[0]) + `}`);

//     // try {
//     //   const response = await amsApi.post(`getChargeMasterList/addConfig`, {
//     //     strParticipantID: participantID,
//     //     strCreatedBy: userid,
//     //     strChargeDescription:
//     //       "Card Issuance Charges,International Transaction,Fuel SuperCharge Amount",
//     //     strAccountType: accountType,
//     //     strChargeType: "CIC,ITC,FSA",

//     //     strAmount: "500,100,200",

//     //     strPercentage: "5,4,3",
//     //   });
//     //   if (response.data.code === "S0000") {
//     //     showSuccess(response.data.message);
//     //   } else {
//     //     showError(response.data.message);
//     //   }
//     // } catch (error) {
//     //   showError(error.response.data.message);
//     // }
//   };

//   // Already Added Charge list

//   useEffect(() => {
//     AlreadyCharges();
//   }, []);
//   const [fetch, setFetch] = useState([]);
//   const AlreadyCharges = async () => {
//     try {
//       const response = await amsApi.post(
//         `accountTypeCharges/getSelectedChargesAcountTypeWise`,
//         {
//           strAccountType: accountType,
//         }
//       );
//       if (response.data.code === "S0000") {
//         setFetch(response.data.chargeMasters);
//         // showSuccess(response.data.message);
//       } else {
//         // showError(response.data.message);
//       }
//     } catch (error) {
//       showError(error);
//     }
//   };

//   return (
//     <AppLayout>
//       <div className="max-w-7xl mx-auto h-full">
//         <div className="min-h-full first-line:flex items-center justify-center ">
//           <div className="w-full ">
//             <div className="flex flex-col ">
//               <h1 className="mx-auto flex justify-center my-2  text-blue-600 text-3xl font-bold">
//                 Charging Configuration
//               </h1>
//             </div>
//           </div>
//           <div className=" md:col-span-2 lg:col-span-1  bg-blue-200">
//             <form className="">
//               <div className="overflow-hidden shadow sm:rounded-md">
//                 <div className=" px-4 py-2 sm:p-2 bg-blue-200  ">
//                   <div className=" justify-between grid gap-6 mb-2 md:grid-cols-3 px-8">
//                     <div>
//                       <label
//                         htmlFor="accountType"
//                         className="block mb-2 text-sm font-medium text-gray-900 dark:text-white"
//                       >
//                         Account Type<span className="text-red-600 px-2">*</span>
//                       </label>
//                       <select
//                         name="accountType"
//                         id="accountType"
//                         value={accountType}
//                         onChange={(e) =>
//                           setAccountType(
//                             e.target.value,
//                             cardrelated(),
//                             TransactionCharg(),
//                             FuelCharg()
//                           )
//                         }
//                         className="mt-1 p-2 block w-full shadow-sm sm:text-sm border-gray-300 rounded-md"
//                       >
//                         <option value="">Select</option>
//                         {alldata.map((data) => (
//                           <optgroup key={uuidv4()}>
//                             <option
//                               className="capatlize text-lg"
//                               // value={}
//                             >
//                               {data.strAccountType || "-"} -{ ""                    }
//                               {data.strDescription || "-"}
//                             </option>
//                           </optgroup>
//                         ))}
//                       </select>
//                     </div>
//                     <div className="flex justify-end  px-4 py-3 text-right mt-3 sm:px-6 ">
//                       <button
//                         type="button"
//                         // onClick={AlreadyCharges}
//                         onClick={openModal}
//                         data-modal-toggle="defaultModal"
//                         className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2.5 px-5  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
//                       >
//                         ALREADY SELECTED CHARGES LIST
//                       </button>
//                     </div>
//                   </div>
//                 </div>
//               </div>
//             </form>
//           </div>

//           <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200   py-2 mt-4 rounded-t-lg ">
//             <h4 className="font-medium text-black text-md">
//               { ""                    }
//               CARD RELATED CHARGES
//             </h4>
//           </div>

//           <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200    py-2  ">
//             <h1 className="font-medium text-black text-md">
//               show{ ""                    }
//               <span>
//                 <input className="w-10"></input>
//               </span>{ ""                    }
//               entries
//             </h1>

//             <div className="flex justify-center rounded-md border border-transparent px-5 mx-4 text-sm font-medium text-white ">
//               <input
//                 type="search"
//                 id="search"
//                 className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
//                 placeholder="Search by Charge Type"
//                 value={searchQuery}
//                 onChange={(e) => setSearchQuery(e.target.value)}
//               />
//             </div>
//           </div>
//           {/* table */}

//           <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
//             <div className="table-wrp block max-h-[27rem] ">
//               <table className="w-full text-sm text-left text-black dark:text-blue-100">
//                 <thead className=" border-b font-medium sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
//                   <tr>
//                     <th scope="col" className="px-6 py-4">
//                       <input
//                         id="default-checkbox"
//                         type="checkbox"
//                         value=""
//                         className="w-4 h-4 text-blue-600 bg-gray-100 border-black rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
//                       />
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       charge type
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       charge description
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       amount
//                     </th>
//                   </tr>
//                 </thead>
//                 <tbody>
//                   {rows
//                     .filter((row) =>
//                       row.strChargeType
//                         .toLowerCase()
//                         .includes(searchQuery.toLowerCase())
//                     )
//                     .map((row) => (
//                       <tr className=" border-b dark:border-neutral-500">
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <input
//                             type="checkbox"
//                             checked={row.isDisabled}
//                             onChange={(event) =>
//                               handleCheckboxChange(event, row.strChargeType)
//                             }
//                           />
//                         </td>
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {row.strChargeType}
//                           </div>
//                         </td>
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {row.strChargeDescription}
//                           </div>
//                         </td>
//                         <td>
//                           <input
//                             type="text"
//                             name="amount"
//                             value={row.amount}
//                             onChange={(event) =>
//                               handleInputChange(event, row.strChargeType)
//                             }
//                             disabled={!row.isDisabled}
//                             className=" p-2 block w-44 shadow-sm sm:text-sm border border-gray-300 rounded-md"
//                           />
//                         </td>
//                       </tr>
//                     ))}
//                 </tbody>
//               </table>
//             </div>
//           </div>

//           {/* Transfer  */}

//           <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200   py-2 mt-4 rounded-t-lg ">
//             <h4 className="font-medium text-black text-md">
//               { ""                    }
//               TRANSACTION RELATED CHARGES
//             </h4>
//           </div>
//           <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200    py-2 mt-4 rounded-t-lg ">
//             <h1 className="font-medium text-black text-md">
//               show{ ""                    }
//               <span>
//                 <input className="w-10"></input>
//               </span>{ ""                    }
//               entries
//             </h1>

//             <div className="flex justify-center rounded-md border border-transparent px-5 mx-4 text-sm font-medium text-white ">
//               <input
//                 type="search"
//                 id="search"
//                 className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
//                 placeholder="Search by Charge Type"
//                 value={searchQuery1}
//                 onChange={(e) => setSearchQuery1(e.target.value)}
//               />
//             </div>
//           </div>

//           <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
//             <div className="table-wrp block max-h-[27rem] ">
//               <table className="w-full text-sm text-left text-black dark:text-blue-100">
//                 <thead className=" border-b font-medium sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
//                   <tr>
//                     <th scope="col" className="px-6 py-4">
//                       <input
//                         id="default-checkbox"
//                         type="checkbox"
//                         value=""
//                         className="w-4 h-4 text-blue-600 bg-gray-100 border-black rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
//                       />
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       charge type
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       charge description
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       amount
//                     </th>
//                   </tr>
//                 </thead>

//                 <tbody>
//                   {list1
//                     .filter((data) =>
//                       data.strChargeType
//                         .toLowerCase()
//                         .includes(searchQuery1.toLowerCase())
//                     )
//                     .map((data) => (
//                       <tr className=" border-b dark:border-neutral-500">
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <input
//                             type="checkbox"
//                             checked={data.isDisabled}
//                             onChange={(event) =>
//                               handleCheckboxChange1(event, data.strChargeType)
//                             }
//                           />
//                         </td>
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {data.strChargeType}
//                           </div>
//                         </td>
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {data.strChargeDescription}
//                           </div>
//                         </td>
//                         <td>
//                           <input
//                             type="text"
//                             name="amount"
//                             value={data.amount}
//                             onChange={(event) =>
//                               handleInputChange1(event, data.strChargeType)
//                             }
//                             disabled={!data.isDisabled}
//                             className=" p-2 block w-44 shadow-sm sm:text-sm border border-gray-300 rounded-md"
//                           />
//                         </td>
//                       </tr>
//                     ))}
//                 </tbody>
//               </table>
//             </div>
//           </div>
//           {/* Fule charge */}

//           <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200   py-2 mt-4 rounded-t-lg ">
//             <h4 className="font-medium text-black text-md">
//               { ""                    }
//               FUEL RELATED CHARGES
//             </h4>
//           </div>
//           <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200    py-2 mt-4 rounded-t-lg ">
//             <h1 className="font-medium text-black text-md">
//               show{ ""                    }
//               <span>
//                 <input className="w-10"></input>
//               </span>{ ""                    }
//               entries
//             </h1>

//             <div className="flex justify-center rounded-md border border-transparent px-5 mx-4 text-sm font-medium text-white ">
//               <input
//                 type="search"
//                 id="search"
//                 className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
//                 placeholder="Search by Charge Type"
//                 value={searchQuery2}
//                 onChange={(e) => setSearchQuery2(e.target.value)}
//               />
//             </div>
//           </div>
//           <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
//             <div className="table-wrp block max-h-[27rem] ">
//               <table className="w-full text-sm text-left text-black dark:text-blue-100">
//                 <thead className=" border-b font-medium sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
//                   <tr>
//                     <th scope="col" className="px-6 py-4">
//                       <input
//                         id="default-checkbox"
//                         type="checkbox"
//                         value=""
//                         className="w-4 h-4 text-blue-600 bg-gray-100 border-black rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
//                       />
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       charge type
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       charge description
//                     </th>
//                     <th scope="col" className="py-4 px-6">
//                       Percentage
//                     </th>
//                   </tr>
//                 </thead>

//                 <tbody>
//                   {list2
//                     .filter((value) =>
//                       value.strChargeType
//                         .toLowerCase()
//                         .includes(searchQuery2.toLowerCase())
//                     )
//                     .map((value) => (
//                       <tr
//                         key={uuidv4()}
//                         className=" border-b dark:border-neutral-500"
//                       >
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <input
//                             type="checkbox"
//                             checked={value.isDisabled}
//                             onChange={(event) =>
//                               handleCheckboxChange2(event, value.strChargeType)
//                             }
//                           />
//                         </td>
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {value.strChargeType}
//                           </div>
//                         </td>
//                         <td className="px-6 py-4 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {value.strChargeDescription}
//                           </div>
//                         </td>
//                         {/*  */}
//                         <td>
//                           <div className="flex ">
//                             <input
//                               type="text"
//                               name="percentage"
//                               defaultValue={value.percentage}
//                               onChange={(event) =>
//                                 handleInputChange2(event, value.strChargeType)
//                               }
//                               disabled={!value.isDisabled}
//                               className="p-2 block w-44 shadow-sm sm:text-sm border border-gray-300 rounded-l-md"
//                             />
//                             <div className="">
//                               <span className="p-2 block w-auto shadow-sm sm:text-sm border border-gray-300 rounded-r-md">
//                                 %
//                               </span>
//                             </div>
//                           </div>
//                         </td>

//                         {/*  */}
//                       </tr>
//                     ))}
//                 </tbody>
//               </table>
//             </div>
//           </div>

//           <div className="bg-gray-100 px-4 py-3 text-right sm:px-6 ">
//             <button
//               type="button"
//               onClick={handleSubmit}
//               data-modal-toggle="defaultModal"
//               className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2.5 px-5  text-sm font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
//             >
//               Submit
//             </button>
//             <button
//               type="button"
//               // onClick={handleClick}
//               className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
//             >
//               Clear
//             </button>
//           </div>
//         </div>
//       </div>

//       {/* Dilog box */}
//       <Transition appear show={isOpen} as={Fragment}>
//         <Dialog as="div" className="relative z-10" onClose={closeModal}>
//           <Transition.Child
//             as={Fragment}
//             enter="ease-out duration-300"
//             enterFrom="opacity-0"
//             enterTo="opacity-100"
//             leave="ease-in duration-200"
//             leaveFrom="opacity-100"
//             leaveTo="opacity-0"
//           >
//             <div className="fixed inset-0 bg-black bg-opacity-25" />
//           </Transition.Child>

//           <div className="fixed inset-0 overflow-y-auto ">
//             <div className="flex min-h-full  items-center justify-center p-4 text-center">
//               <Transition.Child
//                 as={Fragment}
//                 enter="ease-out duration-300"
//                 enterFrom="opacity-0 scale-95"
//                 enterTo="opacity-100 scale-100"
//                 leave="ease-in duration-200"
//                 leaveFrom="opacity-100 scale-100"
//                 leaveTo="opacity-0 scale-95"
//               >
//                 <Dialog.Panel className="w-full md:ml-64  transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
//                   <Dialog.Title
//                     as="h3"
//                     className="text-lg font-medium leading-6 text-blue-900"
//                   >
//                     ALREADY SELECTED CHARGES LIST
//                   </Dialog.Title>
//                   <div className="mt-2  w-full">
//                     <div className="flex justify-between items-center sm:px-6 lg:px-8 bg-blue-200    py-2  ">
//                       <h1 className="font-medium text-black text-md">
//                         show{ ""                    }
//                         <span>
//                           <input className="w-10"></input>
//                         </span>{ ""                    }
//                         entries
//                       </h1>

//                       <div className="flex justify-center rounded-md border border-transparent px-5 mx-4 text-sm font-medium text-white ">
//                         <input
//                           type="search"
//                           id="search"
//                           className="bg-gray-50 border border-gray-300 text-gray-900 text-sm rounded-lg focus:ring-blue-500 focus:border-blue-500 block w-full p-2 dark:bg-gray-700 dark:border-gray-600 dark:placeholder-gray-400 dark:text-white dark:focus:ring-blue-500 dark:focus:border-blue-500"
//                           placeholder="Search by Charge Type"
//                           // value={searchQuery}
//                           // onChange={(e) => setSearchQuery(e.target.value)}
//                         />
//                       </div>
//                     </div>
//                     {/* table */}

//                     <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
//                       <div className="table-wrp block max-h-[27rem] ">
//                         <table className="w-full text-sm text-left text-black dark:text-blue-100">
//                           <thead className=" border-b font-medium sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
//                             <tr>
//                               <th scope="col" className="py-4 px-6">
//                                 charge type
//                               </th>
//                               <th scope="col" className="py-4 px-6">
//                                 charge description
//                               </th>
//                               <th scope="col" className="py-4 px-6">
//                                 amount
//                               </th>
//                               <th scope="col" className="py-4 px-6">
//                                 Percentage
//                               </th>
//                             </tr>
//                           </thead>
//                           <tbody>
//                             {fetch
//                               // .filter((index) =>
//                               //   index.strChargeType
//                               //     .toLowerCase()
//                               //     .includes(searchQuery.toLowerCase())
//                               // )
//                               .map((i) => (
//                                 <tr className=" border-b dark:border-neutral-500">
//                                   <td className="px-6 py-4 whitespace-nowrap"></td>
//                                   <td className="px-6 py-4 whitespace-nowrap">
//                                     <div className="text-smclassName text-gray-900">
//                                       {/* {row.strChargeType} */}
//                                     </div>
//                                   </td>
//                                   <td className="px-6 py-4 whitespace-nowrap">
//                                     <div className="text-smclassName text-gray-900">
//                                       {/* {row.strChargeDescription} */}
//                                     </div>
//                                   </td>
//                                   <td className="px-6 py-4 whitespace-nowrap">
//                                     <div className="text-smclassName text-gray-900">
//                                       {/* {row.strChargeDescription} */}
//                                     </div>
//                                   </td>
//                                   <td className="px-6 py-4 whitespace-nowrap">
//                                     <div className="text-smclassName text-gray-900">
//                                       {/* {row.strChargeDescription} */}
//                                     </div>
//                                   </td>
//                                 </tr>
//                               ))}
//                           </tbody>
//                         </table>
//                       </div>
//                     </div>
//                   </div>

//                   <div className="mt-4 flex space-x-2 justify-end items-end sm:px-2">
//                     <button
//                       type="button"
//                       className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2.5 px-5 mx-4 text-sm font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
//                       onClick={closeModal}
//                     >
//                       Cancel
//                     </button>
//                   </div>
//                 </Dialog.Panel>
//               </Transition.Child>
//             </div>
//           </div>
//         </Dialog>
//       </Transition>
//     </AppLayout>
//   );
// }

// Percentage chrarge type

// <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
//   <div className="table-wrp block max-h-[27rem] ">
//     <table className="w-full text-sm text-left text-black dark:text-blue-100">
//       <thead className=" border-b font-medium sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
//         <tr>
//           <th scope="col" className="px-6 py-4">
//             <input
//               id="default-checkbox"
//               type="checkbox"
//               value=""
//               className="w-4 h-4 text-blue-600 bg-gray-100 border-black rounded focus:ring-blue-500 dark:focus:ring-blue-600 dark:ring-offset-gray-800 focus:ring-2 dark:bg-gray-700 dark:border-gray-600"
//             />
//           </th>
//           <th scope="col" className="py-4 px-6">
//             charge type
//           </th>
//           <th scope="col" className="py-4 px-6">
//             charge description
//           </th>
//           <th scope="col" className="py-4 px-6">
//             Percentage
//           </th>
//         </tr>
//       </thead>

//       <tbody>
//         {list2
//           .filter((value) =>
//             value.strChargeType
//               .toLowerCase()
//               .includes(searchQuery2.toLowerCase())
//           )
//           .map((value) => (
//             <tr key={uuidv4()} className=" border-b dark:border-neutral-500">
//               <td className="px-6 py-4 whitespace-nowrap">
//                 <input
//                   type="checkbox"
//                   checked={value.isDisabled}
//                   onChange={(event) =>
//                     handleCheckboxChange2(event, value.strChargeType)
//                   }
//                 />
//               </td>
//               <td className="px-6 py-4 whitespace-nowrap">
//                 <div className="text-smclassName text-gray-900">
//                   {value.strChargeType}
//                 </div>
//               </td>
//               <td className="px-6 py-4 whitespace-nowrap">
//                 <div className="text-smclassName text-gray-900">
//                   {value.strChargeDescription}
//                 </div>
//               </td>
//               {/*  */}
//               <td>
//                 <div className="flex ">
//                   <input
//                     type="text"
//                     name="percentage"
//                     value={value.percentage}
//                     onChange={(event) =>
//                       handleInputChange2(event, value.strChargeType)
//                     }
//                     disabled={!value.isDisabled}
//                     className="p-2 block w-44 shadow-sm sm:text-sm border border-gray-300 rounded-l-md"
//                   />

//                   <div className="">
//                     <span className="p-2 block w-auto shadow-sm sm:text-sm border border-gray-300 rounded-r-md">
//                       %
//                     </span>
//                   </div>
//                 </div>
//               </td>

//               {/*  */}
//             </tr>
//           ))}
//       </tbody>
//     </table>
//   </div>
// </div>;

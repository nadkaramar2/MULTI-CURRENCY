import AppLayout from "../layout/AppLayout";
import React, { useState, useEffect } from "react";
import amsApi from "../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import CustomAlert from "../layout/CustomAlert";
import AutoPagintation from "../UI/AutoPagintation";
export default function ViewDormantA() {
  const [alldata, setAlldata] = useState([]);
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");
  // Function to handle changes in the search input field
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(1);
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata.filter((data) => {
    const {
      accountNumber,
      accountOpenedDate,
      accountDormantDate,
      custId,
      accountType,
      closingBalance,
      accountHolderName,
    } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

    return (
      (accountNumber &&
        accountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (accountOpenedDate &&
        accountOpenedDate.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (accountDormantDate &&
        accountDormantDate.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (custId && custId.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (accountType &&
        accountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (closingBalance &&
        closingBalance.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (accountHolderName &&
        accountHolderName.toLowerCase().includes(lowerCasedSearchKeyword))
    );
  });
  const highlightKeyword = (text, keyword) => {
    if (!keyword || typeof text !== "string") {
      return text; // No keyword to highlight or invalid text
    }

    const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
    const highlightedText = text.replace(
      regex,
      '<span className="bg-yellow-200">$1</span>'
    );
    return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
  };
  const escapeRegExp = (string) => {
    return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
  };

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");

  const handleShowSuccess = (resMessage) => {
    setAlertTitle("Alert");
    setAlertMessage(resMessage);
    setAlertType("blue");
    setShowAlert(true);
  };

  const handleShowError = (resMessage) => {
    setAlertTitle("Error");
    setAlertMessage(resMessage);
    setAlertType("red");
    setShowAlert(true);
  };

  const handleCloseAlert = () => {
    setShowAlert(false);
  };

  useEffect(() => {
    handleSubmit();
  }, [tcount, itemPage]);

  const handleSubmit = async () => {
    try {
      const response = await amsApi.get(
        `dormancy/listofaccount/pagination/${itemPage}/${tcount}`,

        {}
      );
      if (response.data.code === "S0000") {
        setAlldata(response.data.dormantAccountResponses);
        // handleShowSuccess(response.data.message);
        setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(
      //   "Sorry, the server is under mentaines. Please try again later"
      // );
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full scale-96">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className=" sm: px-11 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xm  leading-normal ">
                View Dormant Account
              </p>
              {/* <div className="flex justify-end items-center sm:px-6 lg:px-8 bg-blue-100  "> */}
              <div className="flex justify-center rounded-md border border-transparent px-2 mx-2 text-sm font-medium text-white ">
                <div className="pt-1 relative mx-auto text-gray-600">
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    value={searchKeyword}
                    onChange={handleSearch}
                    className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search.."
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                  <button
                    type="submit"
                    className="absolute right-0 top-0 mt-3 mr-4"
                  >
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-6 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </button>
                </div>
                {/* </div> */}
              </div>
            </div>
          </div>
        </div>

        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={handleSubmit}
        />
        <div className="overflow-x-auto relative shadow-md ">
          <>
            {" "}
            <div className="table-wrp block max-h-[30rem] max-w-[19rem] ">
              <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
                <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                  <th scope="col" className="py-3 px-11 whitespace-nowrap">
                    ACCOUNT Number
                  </th>
                  <th scope="col" className="  px-11 py-3 whitespace-nowrap">
                    Account Open Date
                  </th>
                  <th scope="col" className="py-3   px-11 whitespace-nowrap">
                    ACCOUnt Dormant Date
                  </th>
                  <th scope="col" className="py-3   px-11   whitespace-nowrap">
                    Cust ID
                  </th>
                  <th scope="col" className="py-3   px-11 whitespace-nowrap">
                    Account Type
                  </th>
                  <th scope="col" className="py-3   px-11 whitespace-nowrap">
                    Closing Balance
                  </th>
                  <th scope="col" className="py-3   px-11 whitespace-nowrap">
                    Account Holder Name
                  </th>
                </thead>
                <tbody>
                  {filteredData.length > 0 ? (
                    <>
                      {filteredData.map(
                        ({
                          accountNumber,
                          accountOpenedDate,
                          accountDormantDate,
                          custId,
                          accountType,
                          closingBalance,
                          accountHolderName,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className=" border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="  px-11 py-3 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  accountNumber || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-11 py-3 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  accountOpenedDate || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-11 py-3 whitespace-nowrap">
                              <div className="text-xs font-medium text-gray-900">
                                {highlightKeyword(
                                  accountDormantDate || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-11 py-3 text-right whitespace-nowrap">
                              <div className="text-xs  font-medium text-gray-900">
                                {highlightKeyword(custId || "-", searchKeyword)}
                              </div>
                            </td>
                            <td className="  px-11 py-3  whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  accountType || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-11 py-3 text-right whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  closingBalance || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                            <td className="  px-11 py-3  whitespace-nowrap ">
                              <div className="text-xs  font-medium text-gray-900 ">
                                {highlightKeyword(
                                  accountHolderName || "-",
                                  searchKeyword
                                )}
                              </div>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="12" className=" px-6 py-1 text-center">
                        <span className="text-sm font-normal text-gray-500 ">
                          No data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </>
        </div>
      </div>
      {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
      {/* Your existing code here */}

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
    </AppLayout>
  );
}
// import React, { useState, useEffect } from "react";
// import AppLayout from "../layout/AppLayout";
// import amsApi from "../api/amsApi";
// import { v4 as uuidv4 } from "uuid";
// import AutoPagintation from "../UI/AutoPagintation";
// import CustomAlert from "../layout/CustomAlert";
// export default function ViewDormantA() {
//   const [fetchData, setFetchData] = useState([]);
//   const storedToken = localStorage.getItem("token");
//   const [itemPage, setItemPage] = useState(10);
//   const [tcount, setTcount] = useState(1);
//   const [searchKeyword, setSearchKeyword] = useState("");
//   const [showAlert, setShowAlert] = useState(false);
//   const [alertTitle, setAlertTitle] = useState("");
//   const [alertMessage, setAlertMessage] = useState("");
//   const [alertType, setAlertType] = useState("");
//   // Function to handle changes in the search input field
//   const handleSearch = (event) => {
//     setSearchKeyword(event.target.value);
//   };

//   // Filter the acceptData array based on the searchKeyword
//   const filteredData = fetchData.filter((data) => {
//     const {
//       strAccountType,
//       strAccountNumber,
//       strCustId,
//       strAccountHolderName,
//       strCid,
//       bid,
//       strClosingBalance,
//     } = data;
//     const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

//     return (
//       (strAccountType &&
//         strAccountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
//       (strAccountNumber &&
//         strAccountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
//       (strCustId &&
//         strCustId.toLowerCase().includes(lowerCasedSearchKeyword)) ||
//       (strAccountHolderName &&
//         strAccountHolderName.toLowerCase().includes(lowerCasedSearchKeyword)) ||
//       (strCid && strCid.toLowerCase().includes(lowerCasedSearchKeyword)) ||
//       (bid && bid.toLowerCase().includes(lowerCasedSearchKeyword)) ||
//       (strClosingBalance &&
//         strClosingBalance.toLowerCase().includes(lowerCasedSearchKeyword))
//     );
//   });
//   const highlightKeyword = (text, keyword) => {
//     if (!keyword || typeof text !== "string") {
//       return text; // No keyword to highlight or invalid text
//     }

//     const regex = new RegExp(`(${escapeRegExp(keyword)})`, "gi");
//     const highlightedText = text.replace(
//       regex,
//       '<span className="bg-yellow-200">$1</span>'
//     );
//     return <div dangerouslySetInnerHTML={{ __html: highlightedText }} />;
//   };
//   const escapeRegExp = (string) => {
//     return string.replace(/[.*+?^${}()|[\]\\]/g, "\\$&"); // Escape special characters
//   };

//   const handleShowSuccess = (resMessage) => {
//     setAlertTitle("Alert");
//     setAlertMessage(resMessage);
//     setAlertType("blue");
//     setShowAlert(true);
//   };

//   const handleShowError = (resMessage) => {
//     setAlertTitle("Error");
//     setAlertMessage(resMessage);
//     setAlertType("red");
//     setShowAlert(true);
//   };

//   const handleCloseAlert = () => {
//     setShowAlert(false);
//   };
//   useEffect(() => {
//     ViewAccountCollectedDeatils();
//   }, [tcount]);
//   const ViewAccountCollectedDeatils = async () => {
//     try {
//       const response = await amsApi.get(
//         `dormancy/listofaccount/pagination/${itemPage}/${tcount}`,
//         {},

//         {
//           headers: {
//             "Content-Type": "application/json",
//             Authorization: `${storedToken}`,
//           },
//         }
//       );
//       if (response.data.code === "S0000") {
//         setFetchData(response.data.dormantAccountResponses);
//       } else {
//         handleShowError(response.data.message);
//       }
//     } catch (error) {
//        handleShowError(
//   "Sorry, the server is under mentaines. Please try again later"
// );
//     }
//   };

//   return (
//     <AppLayout>
//       <div className="mx-4">
//         <div className="w-full shadow-md mt-1">
//           <div className="px-4  sm:px-10  bg-blue-200">
//             <div className=" flex  items-center justify-between">
//               <p className="text-sm  sm:text-xm text-black :text-2xl  leading-normal ">
//                 View Dormant Account
//               </p>
//             </div>
//           </div>
//         </div>
//         <div className=" md:col-span-2 lg:col-span-1  ">
//           <div className="overflow-hidden shadow  ">
//             <div className=" px-4  sm:p-2 bg-white  ">
//               <div className="grid gap-8 lg:grid-cols-4 md:grid-cols-3 sm:grid-cols-1">
//                 <div className="flex justify-center rounded-md border border-transparent px-2  text-sm font-medium text-white ">
//                   {/* <div className=" relative mx-auto text-gray-600">
//                     <input
//                       title="Search Data"
//                       type="search"
//                       id="search"
//                       value={searchKeyword}
//                       onChange={handleSearch}
//                       className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
//                       placeholder="Search.."
//                     />
//                     <button
//                       type="submit"
//                       className="absolute right-0 top-0 mt-2 mr-1 px-1 "
//                     >
//                       <svg
//                         xmlns="http://www.w3.org/2000/svg"
//                         fill="none"
//                         viewBox="0 0 24 24"
//                         strokeWidth={1.5}
//                         stroke="currentColor"
//                         className="w-6 h-6"
//                       >
//                         <path
//                           strokeLinecap="round"
//                           strokeLinejoin="round"
//                           d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
//                         />
//                       </svg>
//                     </button>
//                   </div> */}
//                 </div>
//               </div>
//             </div>
//           </div>
//         </div>

//         <AutoPagintation
//           addPageperData={setItemPage}
//           addCurrentPage={setTcount}
//           pagePerData={itemPage}
//           currentPage={tcount}
//           tcount={filteredData[0]?.strTotalCount}
//           reCallApi={ViewAccountCollectedDeatils}
//         />
//         {/* Table */}
//         <div className="overflow-x-auto relative shadow-md ">
//           <div className="table-wrp block max-h-[27rem] ">
//             <table className="w-full text-xs text-left text-clack dark:text-blue-100">
//               <thead className="text-xs border-b sticky top-0 text-black uppercase bg-gray-100 dark:text-white">
//                 <th scope="col" className="py-3.5 px-5">
//                   Account Number
//                 </th>
//                 <th scope="col" className="py-3.5 px-5">
//                   Account Holder Name
//                 </th>
//                 <th scope="col" className="py-3.5 px-5">
//                   Cust Id
//                 </th>
//                 <th scope="col" className="py-3.5 px-5">
//                   Account Type
//                 </th>
//                 <th scope="col" className="py-3.5 px-5">
//                   Account Open Date
//                 </th>
//                 <th scope="col" className="py-3.5 px-5">
//                   Account Dormant Date
//                 </th>
//                 <th scope="col" className="py-3.5 px-5">
//                   Closing Balance
//                 </th>
//               </thead>
//               <tbody>
//                 {filteredData.map((data) => (

//                   <tr
//                     key={uuidv4()}
//                     className=" border-b dark:border-neutral-500"
//                   >
//                     <td className="px-5 py-3.5 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           data.accountNumber || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-5 py-3.5 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           data.accountHolderName || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-5 py-3.5 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(data.custId || "-", searchKeyword)}
//                       </div>
//                     </td>
//                     <td className="px-5 py-3.5 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           data.accountType || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-5 py-3.5 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           data.accountOpenedDate || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-5 py-3.5 whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           data.accountDormantDate || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                     <td className="px-5 py-3.5  text-end whitespace-nowrap">
//                       <div className="text-sm font-medium text-gray-900">
//                         {highlightKeyword(
//                           data.closingBalance || "-",
//                           searchKeyword
//                         )}
//                       </div>
//                     </td>
//                   </tr>
//                 ))}
//               </tbody>
//             </table>
//           </div>
//         </div>
//       </div>

//       {showAlert && (
//         <CustomAlert
//           title={alertTitle}
//           message={alertMessage}
//           type={alertType}
//           onClose={handleCloseAlert}
//         />
//       )}
//     </AppLayout>
//   );
// }

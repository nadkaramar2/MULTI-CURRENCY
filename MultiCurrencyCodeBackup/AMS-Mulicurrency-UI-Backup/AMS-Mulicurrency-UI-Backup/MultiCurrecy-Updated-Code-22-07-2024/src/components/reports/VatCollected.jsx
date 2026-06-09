// import React, { useState, useEffect } from "react";
// import AppLayout from "../../layout/AppLayout";
// import amsApi from "../../api/amsApi";
// import { v4 as uuidv4 } from "uuid";
// import CustomAlert from "../../layout/CustomAlert";
// import AutoPagintation from "../../UI/AutoPagintation";
// import AutoPagintationReport from "./AutoPagintationReport";
// export default function VatCollected() {
//   const [fetchData, setFetchData] = useState([]);
//   const storedToken = localStorage.getItem("token");

//   const [showAlert, setShowAlert] = useState(false);
//   const [alertTitle, setAlertTitle] = useState("");
//   const [alertMessage, setAlertMessage] = useState("");
//   const [alertType, setAlertType] = useState("");
//  const [searchQuery, setSearchQuery] = useState("");
//  const [itemPage, setItemPage] = useState(20);
//  const [tcount, setTcount] = useState(1);
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

//   // DATE AND TIME
//   var currentdate = new Date();
//   var time =
//     currentdate.getHours() +
//     ":" +
//     currentdate.getMinutes() +
//     ":" +
//     currentdate.getSeconds();

//   var date =
//     currentdate.getDate() +
//     "/" +
//     (currentdate.getMonth() + 1) +
//     "/" +
//     currentdate.getFullYear();

//   useEffect(() => {
//     VatCollectedDeatils();
//   }, [tcount, itemPage]);

//   const VatCollectedDeatils = async () => {
//     try {
//       const response = await amsApi.post(
//         `vatTypeMaster/getVatCollected/Pagination/${itemPage}/${tcount}`,
//         {},
//         {
//           headers: {
//             "Content-Type": "application/json",
//             Authorization: `${storedToken}`,
//           },
//         }
//       );
//       if (response.data.code === "S0000") {
//         setFetchData(response.data.vatCollectedList);
//       } else {
//         handleShowError(response.data.message);
//       }
//     } catch (error) {
//       handleShowError(error.response.data.message);
//     }
//   };

//    useEffect(() => {
//       if (searchQuery === "" || searchQuery === " ") {
//           VatCollectedDeatils();
//       }
//     }, [searchQuery]);

//   const Search = async () => {
//       try {
//       const response = await amsApi.post(
//         `vatTypeMaster/getVatCollectedSearch/pagination/20/1`,
//         {
//           keyword: searchQuery,
//         }
//       );
//       if (response.data.code === "S0000") {
//         setFetchData(response.data.vatCollectedList);
//         // handleShowSuccess(response.data.message);
       
//         } else {
//           handleShowError(response.data.message);
         
//         }
//       } catch (error) {
//         // handleShowError(error.response.data.message);
//       }
    
//   };

//   const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
//   const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order

//   const handleSort = (column) => {
//     if (column === sortColumn) {
//       // If clicking on the same column, toggle the sorting order
//       setSortOrder(sortOrder === "asc" ? "desc" : "asc");
//     } else {
//       // If clicking on a different column, set the new column and default to ascending order
//       setSortColumn(column);
//       setSortOrder("asc");
//     }
//   };

//   const sortedData = [...fetchData].sort((a, b) => {
//     if (sortOrder === "asc") {
//       const aValue = a[sortColumn] || "";
//       const bValue = b[sortColumn] || "";
//       return aValue.localeCompare(bValue);
//     } else {
//       const aValue = a[sortColumn] || "";
//       const bValue = b[sortColumn] || "";
//       return bValue.localeCompare(aValue);
//     }
//   });
//   const renderSortArrow = (column) => {
//     const arrowStyle = {
//       cursor: "pointer",
//       transition: "transform 0.2s",
//     };
//     return (
//       <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
//         {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
//       </span>
//     );
//   };

//   return (
//     <AppLayout>
//       <div className="max-w-full mx-auto h-full scale-96">
//         <div className="w-full shadow-md mt-2 bg-blue-200">
//           <div className=" sm: px-10 rounded-t-lg ">
//             <div className=" flex  items-center justify-between">
//               <p className="text-base  sm:text-xm text-black :text-2xm  leading-normal ">
//                 Vat Collected
//               </p>
//             </div>
//           </div>
//         </div>
//         <div className=" md:col-span-2 lg:col-span-1 ">
//           <div className="overflow-hidden shadow ">
//             <div className=" px-4  sm:p-2 bg-white ">
//               <div className="gap-4 md:grid-cols-2  justify-between flex ">
//                 <div>
//                   {" "}
//                   <div className=" w-full px-1 py-1 text-base font-normal text-gray-700 ">
//                     Balance as on {date} {time}
//                   </div>
//                 </div>

              
//               </div>
//             </div>
//           </div>
//         </div>

//         <AutoPagintationReport
//           addPageperData={setItemPage}
//           addCurrentPage={setTcount}
//           searchData={setSearchQuery}
//           pagePerData={itemPage}
//           currentPage={tcount}
//           tcount={fetchData[0]?.strTotalCount}
//           reCallApi={Search}
//         />
//         {/* <AutoPagintation
//           addPageperData={setItemPage}
//           addCurrentPage={setTcount}
//           pagePerData={itemPage}
//           currentPage={tcount}
//           tcount={fetchData[0]?.strTotalCount}
//           reCallApi={VatCollectedDeatils}
//         /> */}
//         {/* Table */}
//         <div className="overflow-x-auto relative shadow-md ">
//           <>
//             {" "}
//             <div className="table-wrp block max-h-[27rem] max-w-[33rem] ">
//               <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
//                 <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
//                   <th
//                     scope="col"
//                     className="py-3 px-20 whitespace-nowrap"
//                     onClick={() => handleSort("vatType")}
//                   >
//                     Vat Type {renderSortArrow("vatType")}
//                   </th>
//                   <th
//                     scope="col"
//                     className="py-3 px-20 whitespace-nowrap"
//                     onClick={() => handleSort("vatDescription")}
//                   >
//                     Description {renderSortArrow("vatDescription")}
//                   </th>
//                   <th
//                     scope="col"
//                     className="py-3 px-20 whitespace-nowrap"
//                     onClick={() => handleSort("glAccountNo")}
//                   >
//                     GL Account No {renderSortArrow("glAccountNo")}
//                   </th>
//                   <th
//                     scope="col"
//                     className="py-3 px-20 whitespace-nowrap"
//                     onClick={() => handleSort("glAccountBalance")}
//                   >
//                     Account Balance {renderSortArrow("glAccountBalance")}
//                   </th>
//                 </thead>
//                 <tbody>
//                   {sortedData
//                     .filter(
//                       (data) =>
//                         data.vatType
//                           .toLowerCase()
//                           .includes(searchQuery.toLowerCase()) ||
//                         data.vatDescription
//                           .toLowerCase()
//                           .includes(searchQuery.toLowerCase()) ||
//                         data.glAccountNo
//                           .toLowerCase()
//                           .includes(searchQuery.toLowerCase()) ||
//                         data.glAccountBalance
//                           .toLowerCase()
//                           .includes(searchQuery.toLowerCase())
//                     )
//                     .map((data) => (
//                       <tr
//                         key={uuidv4()}
//                         className=" border-b dark:border-neutral-500"
//                       >
//                         <td className="px-20 py-3 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {data.vatType || "-"}
//                           </div>
//                         </td>
//                         <td className="px-20 py-3 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {data.vatDescription || "-"}
//                           </div>
//                         </td>
//                         <td className="px-20 py-3 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {data.glAccountNo || "-"}
//                           </div>
//                         </td>
//                         <td className="px-20 py-3 whitespace-nowrap">
//                           <div className="text-smclassName text-gray-900">
//                             {data.glAccountBalance || "-"}
//                           </div>
//                         </td>
//                       </tr>
//                     ))}
//                 </tbody>
//               </table>
//             </div>
//           </>
//         </div>
//       </div>

//       {/* Your existing code here */}

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


import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import { v4 as uuidv4 } from "uuid";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import amsApi from "../../api/amsApi";
export default function VatCollected() {
 

  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);
  const [duration, setDuration] = useState("");
  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [alldata, setallData] = useState([]);
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const [itemPage, setItemPage] = useState("10");
  const [tcount, setTcount] = useState("1");
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

  // dration

  const yesterday = new Date();
  function remainDays(leftDate) {
    yesterday.setDate(yesterday.getDate() - leftDate);
  }
  function padTo2Digits(num) {
    return num.toString().padStart(2, "0");
  }

  function formatDate(date) {
    return [
      date.getFullYear(),

      padTo2Digits(date.getMonth() + 1),

      padTo2Digits(date.getDate()),
    ].join("-");
  }
  const current = new Date();

  const date = `${current.getFullYear()}-${String(
    current.getMonth() + 1
  ).padStart(2, "0")}-${String(current.getDate()).padStart(2, "0")}`;

  const daysInCurrentMonth = `${String(current.getDate()).padStart(2, "0")}`;

  function endOfMonth(date) {
    return new Date(date.getFullYear(), date.getMonth() + 0, 0);
  }

  function startOfMonth(date) {
    return new Date(date.getFullYear(), date.getMonth() - 1, 1);
  }

  const DurationHandler = (e) => {
    setDateOpen(false);
    const selectData = e.target.value;
    setDuration(selectData);
    if (selectData === "0") {
      setStartdate(date);
      setEnddata(date);
    } else if (selectData === "1") {
      remainDays(1);
      setStartdate(formatDate(yesterday));
      setEnddata(formatDate(yesterday));
    } else if (selectData === "7") {
      remainDays(7);
      setStartdate(formatDate(yesterday));
      setEnddata(date);
    } else if (selectData === "10") {
      remainDays(10);
      setStartdate(formatDate(yesterday));
      setEnddata(date);
    } else if (selectData === "custom") {
      setDateOpen(true);
    } else if (selectData === "cMonth") {
      remainDays(parseInt(daysInCurrentMonth) - 1);
      setStartdate(formatDate(yesterday));
      setEnddata(date);
    } else if (selectData === "pMonth") {
      setStartdate(formatDate(startOfMonth(current)));
      setEnddata(formatDate(endOfMonth(current)));
    }
  };

   const [selectedOption, setSelectedOption] = useState("ALL");
   const [accountno, setAccountno] = useState("");
   const handleOptionChange = (event) => {
     setSelectedOption(event.target.value);
   };

   const handleAccountTypeChange = (event) => {
     setAccountno(event.target.value);
   };

  useEffect(() => {
    handleSubmit();
    setError("");
  }, [tcount, itemPage]);
  const handleSubmit = async () => {
   let apiRequestData;
   if (selectedOption === "ALL") {
     apiRequestData = "ALL";
   } else  if (selectedOption === "custom1") {
     apiRequestData = accountno;
   }
    if (duration === "" ) {
      setError(true);
    } else if (startdate > enddate) {
      handleShowError("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `revolvingCreditCardTxn/getBilledData/pagination/${itemPage}/${tcount}`,
          {
            strAccountNumber: apiRequestData,
            fromDate: startdate,
            toDate: enddate,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setallData(response.data.getRevolvingData);
          setCheckpoint(1);
          setChecked(false);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
          setallData([]);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
        setallData([]);
      }
    }
  };

  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setAccountno("");

    setError("");
    setDuration("");
  };

  // Sorted Tabled data by Date and time wise
  const [sortOrder, setSortOrder] = useState("asc");
  const [sortColumn, setSortColumn] = useState("");
  const handleSort = (column) => {
    if (column === sortColumn) {
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      setSortColumn(column);
      setSortOrder("asc");
    }
  };
  const sortedData = alldata.slice(0).sort((a, b) => {
    const dateA = new Date(a.txnDate);
    const dateB = new Date(b.txnDate);
    const timeA = new Date("1970/01/01 " + a.txnTime);
    const timeB = new Date("1970/01/01 " + b.txnTime);
    if (dateA.getTime() === dateB.getTime()) {
      return sortOrder === "asc" ? timeA - timeB : timeB - timeA;
    } else {
      return sortOrder === "asc" ? dateA - dateB : dateB - dateA;
    }
  });

  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer   text-md hover:bg-blue-500  hover:text-white px-1 mx-1">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Billed Transaction
              </p>
              <div className="flex justify-end rounded-md border border-transparent px-1  text-sm font-medium text-white ">
                <div className=" relative mx-auto text-gray-600">
                  <span type="submit" className="absolute left-0 top-0  px-1 ">
                    <svg
                      xmlns="http://www.w3.org/2000/svg"
                      fill="none"
                      viewBox="0 0 24 24"
                      strokeWidth={1.5}
                      stroke="currentColor"
                      className="w-5 h-6"
                    >
                      <path
                        strokeLinecap="round"
                        strokeLinejoin="round"
                        d="M21 21l-5.197-5.197m0 0A7.5 7.5 0 105.196 5.196a7.5 7.5 0 0010.607 10.607z"
                      />
                    </svg>
                  </span>
                  <input
                    title="Search Data"
                    type="search"
                    id="search"
                    value={searchQuery}
                    onChange={(e) => setSearchQuery(e.target.value)}
                    className="block w-full pl-10 pr-1 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                    placeholder="Search"
                    autoComplete="off"
                    style={{
                      backgroundColor: checkpoint === 1 ? "white" : "lightgray",
                    }}
                    disabled={checkpoint !== 1}
                  />
                </div>
                <button className="inline-flex sm:ml-3  sm:mt-0 items-start justify-start px-2 focus:outline-none rounded text-xs font-medium">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
                    inputProps={{ "aria-label": "Switch A" }}
                    slotProps={{
                      track: {
                        children: (
                          <React.Fragment>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ ml: "10px" }}
                            >
                              On
                            </Typography>
                            <Typography
                              component="span"
                              level="inherit"
                              sx={{ mr: "8px" }}
                            >
                              Off
                            </Typography>
                          </React.Fragment>
                        ),
                      },
                    }}
                    sx={{
                      "--Switch-thumbSize": "27px",
                      "--Switch-trackWidth": "64px",
                      "--Switch-trackHeight": "31px",
                    }}
                  />
                </button>
              </div>
            </div>
          </div>
        </div>

        {checked && (
          <div className=" md:col-span-2 lg:col-span-1  ">
            <form className="">
              <div className="overflow-hidden shadow sm:rounded-md ">
                <div className=" px-4  bg-white ">
                  <div className="grid gap-2 lg:grid-cols-5 px-5">
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Select
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="selectedOption"
                        name="selectedOption"
                        onChange={(e) => handleOptionChange(e)}
                        value={selectedOption}
                        className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        <option value="ALL">ALL</option>
                        <option value="custom1">Custom</option>
                      </select>
                      {error && selectedOption.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Type!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    {selectedOption === "custom1" && (
                      <>
                        <div>
                          <label
                            htmlFor="number"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Account Number{""}
                            <span className="text-red-600 px-2">*</span>
                          </label>

                          <input
                            type="text"
                            id="accountno"
                            autoComplete="off"
                            value={accountno}
                            onChange={handleAccountTypeChange}
                            // onChange={(e) => {
                            //   const value = e.target.value;

                            //   setAccountno(value);

                            //   const regex = /^[0-9]*$/;

                            //   if (!regex.test(value)) {
                            //     setErrorMessage(" Only allow numeric digits");
                            //   } else {
                            //     setErrorMessage("");
                            //   }
                            //   setError("");
                            // }}
                            // onBlur={() => {
                            //   if (accountno.length === 0) {
                            //     // setError("Please enter Account Number!");
                            //     setErrorMessage("");
                            //   }
                            // }}
                            className="block w-full px-2 py-1 text-sm  font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                            placeholder="Enter Account Number"
                          />

                          {errorMessage ? (
                            <p className="text-red-500 text-xs font-medium">
                              {errorMessage}
                            </p>
                          ) : error && accountno.length <= 0 ? (
                            <p className="text-red-500 text-xs font-medium">
                              Please Enter Account Number!
                            </p>
                          ) : null}
                        </div>
                      </>
                    )}

                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Duration
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        id="duration"
                        name="duration"
                        value={duration}
                        onChange={(e) => DurationHandler(e)}
                        className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        <option value="0">Today</option>
                        <option value="1">Yesterday</option>
                        <option value="7">Last 7 Days</option>
                        <option value="10">Last 10 Days</option>
                        <option value="cMonth">Current Month</option>
                        <option value="pMonth">Previous Month</option>
                        <option value="custom">Custom</option>
                      </select>
                      {error && duration.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Duration
                        </p>
                      ) : (
                        ""
                      )}
                    </div>

                    {dateOpen && (
                      <>
                        <div>
                          <label
                            htmlFor="startdate"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Start Date
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
                            id="startdate"
                            value={startdate}
                            onChange={(e) => setStartdate(e.target.value)}
                            className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                          {error && startdate.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Enter From Date
                            </p>
                          ) : (
                            ""
                          )}
                        </div>
                        <div>
                          <label
                            htmlFor="enddate"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            End Date
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
                            id="enddate"
                            value={enddate}
                            onChange={(e) => setEnddata(e.target.value)}
                            className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                          {error && enddate.length <= 0 ? (
                            <p className="text-red-500   text-xs font-medium">
                              Please Enter To date
                            </p>
                          ) : (
                            ""
                          )}
                        </div>
                      </>
                    )}

                    <div className="">
                      <button
                        title="Click Search Button"
                        type="button"
                        onClick={handleSubmit}
                        data-modal-toggle="defaultModal"
                        className="inline-flex justify-center  bg-blue-500 hover:bg-blue-700 b  focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 border-blue-700 py-1.5 px-5 mx-2 my-3    text-xs font-bold text-white rounded "
                      >
                        <MagnifyingGlassIcon className="h-4 w-3" />
                      </button>
                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center  bg-rose-500 hover:bg-rose-700 b  focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 border-rose-700 py-1.5 px-5 mx-2 my-3  text-xs font-bold text-white rounded "
                      >
                        <XMarkIcon className="h-4 w-3" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        )}

        <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={handleSubmit}
        />

        <div className="bg-white overflow-x-auto relative shadow-md ">
          <div className="table-wrap block max-h-[27rem] max-w-5xl  ">
            <table className="w-full  text-xs text-left text-clack dark:text-blue-100  ">
              <thead className="text-xm border-b sticky top-0 text-gray-400  bg-gray-100 dark:text-white">
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strTxnId")}
                >
                  Txn Id {renderSortArrow("strTxnId")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strTranType")}
                >
                  Tran Type {renderSortArrow("strTranType")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("txnDate")}
                >
                  Txn Date {renderSortArrow("txnDate")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("txnTime")}
                >
                  Txn Time {renderSortArrow("txnTime")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strAccountType")}
                >
                  Account Type {renderSortArrow("strAccountType")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strAccountNumber")}
                >
                  Account Number {renderSortArrow("strAccountNumber")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strTxnAmount")}
                >
                  Txn Amount {renderSortArrow("strTxnAmount")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strTranTypeDes")}
                >
                  Tran Type Description {renderSortArrow("strTranTypeDes")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strMcc")}
                >
                  Mcc{renderSortArrow("strMcc")}
                </th>

                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strUpdateTxnAmount")}
                >
                  Update Txn Amount
                  {renderSortArrow("strUpdateTxnAmount")}
                </th>
                <th
                  scope="col"
                  className="py-2.5 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strRemaningGracePeriod")}
                >
                  Remaning Grace Period
                  {renderSortArrow("strRemaningGracePeriod")}
                </th>
              </thead>

              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map((data) => (
                      <tr
                        key={uuidv4()}
                        className="border-b text-center dark:border-neutral-500"
                      >
                        <td className="px-2 py-2.5 whitespace-nowrap">
                          <span className="text-xs font-sm text-gray-900">
                            {data.strTxnId || "-"}
                          </span>
                        </td>
                        <td className="px-2 py-2.5 whitespace-nowrap">
                          <span className="text-xs font-sm text-gray-900">
                            {data.strTranType || "-"}
                          </span>
                        </td>
                        <td className="px-2 py-2.5 whitespace-nowrap">
                          <span className="text-xs font-sm text-gray-900">
                            {data.txnDate || "-"}
                          </span>
                        </td>
                        <td className="whitespace-nowrap px-2 py-2.5">
                          <span className="text-xs font-sm text-gray-900">
                            {data.txnTime || "-"}
                          </span>
                        </td>

                        <td className="px-2 py-2.5 whitespace-nowrap">
                          <span className="text-xs font-sm text-gray-900">
                            {data.strAccountType || "-"}
                          </span>
                        </td>

                        <td className="px-2 py-2.5  whitespace-nowrap">
                          <span className="text-xs font-sm  text-gray-900">
                            {data.strAccountNumber || "-"}
                          </span>
                        </td>
                        <td className="px-2 py-2.5 text-end whitespace-nowrap">
                          <span className="text-xs  font-sm  text-gray-900">
                            {data.strTxnAmount || "-"}
                          </span>
                        </td>
                        <td className="px-2 py-2.5  whitespace-nowrap">
                          <span className="text-xs font-sm  text-gray-900">
                            {data.strTranTypeDes || "-"}
                          </span>
                        </td>

                        <td className="px-2 py-2.5 whitespace-nowrap">
                          <span className="text-xs font-sm text-gray-900">
                            {data.strMcc || "-"}
                          </span>
                        </td>

                        <td className="px-2 py-2.5  text-end whitespace-nowrap">
                          <span className="text-xs font-sm text-gray-900">
                            {data.strUpdateTxnAmount || "-"}
                          </span>
                        </td>
                        <td className="px-2 py-2.5 whitespace-nowrap">
                          <span className="text-xs font-sm text-gray-900">
                            {data.strRemaningGracePeriod || "-"}
                          </span>
                        </td>
                      </tr>
                    ))}
                  </>
                ) : (
                  <tr>
                    <td colSpan="8" className="px-2 py-1.5 text-center">
                      <span className="text-sm font-normal text-gray-500">
                        No data available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
        </div>
      </div>

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




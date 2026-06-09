/* eslint-disable react/jsx-no-undef */
import AppLayout from "../../layout/AppLayout";
import React, { Fragment, useState, useEffect } from "react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { Dialog, Transition } from "@headlessui/react";
import { EyeIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import { DialogContentText, DialogTitle } from "@mui/material";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import swal from "sweetalert";
import AutoPagintationReport from "./AutoPagintationReport";
export default function TransactionRequestResponse() {
  const [alldata, setAlldata] = useState([]);
  const [viewTxn, setViewTxn] = useState("");
  const [viewTxnid, setViewTxnid] = useState("");
  const [viewTxndata, setViewTxndata] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const [searchKeyword, setSearchKeyword] = useState("");
   const [searchQuery, setSearchQuery] = useState("");
   const [itemPage, setItemPage] = useState(20);
   const [tcount, setTcount] = useState(1);
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  // const [proCategoryData, setProCategoryData] = useState({});
  // Function to handle changes in the search input field
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
  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // view model
  let [isShow, setIsShow] = useState(false);
  function closeProCategoryViewModel() {
    setIsShow(false);
  }
  function openProCategoryViewModel() {
    setIsShow(true);
  }
  const viewTxnContent = async (reqTxnDate, montraTxnId, txnRequest) => {
    console.log(reqTxnDate, montraTxnId, txnRequest);
    openProCategoryViewModel(reqTxnDate);
    setViewTxnid(montraTxnId);
    setViewTxn(txnRequest);
  };
  // view model
  let [isShow1, setIsShow1] = useState(false);
  function closeProCategoryViewModel1() {
    setIsShow1(false);
  }
  function openProCategoryViewModel1() {
    setIsShow1(true);
  }
  const viewTxnResponsed = async (
    code,
    message,
    status,
    responseCode,
    transactionDate,
    authCode,
    montraTxnId,
    amsTransactionId,
    agentAvailableBalance,
    txnResponse
  ) => {
    console.log(
      code,
      message,
      status,
      responseCode,
      transactionDate,
      authCode,
      montraTxnId,
      amsTransactionId,
      agentAvailableBalance,
      txnResponse
    );
    openProCategoryViewModel1(amsTransactionId);
    setViewTxndata(txnResponse);
  };
  const [error, setError] = useState("");
  useEffect(() => {
    setError("");
  }, [itemPage, tcount]);
  const viewTxnRequest = async () => {
    // openProCategoryViewModel();
    if (
      startdate === "" ||
      startdate.length === 0 ||
      enddate === "" ||
      enddate.length === 0 ||
      duration === ""
    ) {
      setError(true);
    } else if (startdate > enddate) {
      swal("please Choose Lesser Date From To Date");
    } else {
      try {
        const response = await amsApi.post(
          `transactionReqResLog/getTxnReqResLogsListDataPag/pagination/${itemPage}/${tcount}`,
          {
            fromDate: startdate,
            toDate: enddate,
            fromTime: "00:00:00",
            toTime: "00:00:59",
          }
        );
        if (response.data.code === "S0000") {
          setAlldata(response.data.getTxnReqResLogsList);
         
          // handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(
        //   "Sorry, the server is under mentaines. Please try again later"
        // );
      }
    }
  };

    useEffect(() => {
      if (searchQuery === "" || searchQuery === " ") {
        viewTxnRequest();
        setError("");
      }
    }, [searchQuery]);

    const Search = async () => {
      
        try {
          const response = await amsApi.post(
            `transactionReqResLog/getTxnReqResLogsSearch/pagination/20/1`,
            {
              fromDate: startdate,
              toDate: enddate,
              keyword: searchQuery,
            }
          );
          if (response.data.code === "S0000") {
            setAlldata(response.data.getTxnReqResLogsList);
         
            // handleShowSuccess(response.data.message);
          } else {
            handleShowError(response.data.message);
          }
        } catch (error) {
          // handleShowError(error.response.data.message);
        }
    
    };
  const [dateOpen, setDateOpen] = useState(false);
  const [duration, setDuration] = useState("");
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
  // sorted table data
  const [sortColumn, setSortColumn] = useState(""); // State to track the sorted column
  const [sortOrder, setSortOrder] = useState("asc"); // State to track sorting order
  const handleSort = (column) => {
    if (column === sortColumn) {
      // If clicking on the same column, toggle the sorting order
      setSortOrder(sortOrder === "asc" ? "desc" : "asc");
    } else {
      // If clicking on a different column, set the new column and default to ascending order
      setSortColumn(column);
      setSortOrder("asc");
    }
  };
  // Sort the data based on the chosen column and order
  const sortedData = [...alldata].sort((a, b) => {
    if (sortOrder === "asc") {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return aValue.localeCompare(bValue);
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.localeCompare(aValue);
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
      <div className="">
        <div className="w-full shadow-md mt-2">
          <div className="px-14 py-1 sm:px-14  bg-blue-200">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Txn Request Response
              </p>
            </div>
          </div>
        </div>
        <div className=" md:col-span-2 lg:col-span-1  ">
          <div className="overflow-hidden shadow ">
            <div className=" px-2  sm:p-2 bg-white  ">
              <div className="grid gap-4  md:grid-cols-4  px-5">
                <div>
                  <label
                    htmlFor="duration"
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
                    className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
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
                        htmlFor="Start Date"
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
                        className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder=""
                      />
                      {error && startdate.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please select Start Date
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    <div>
                      <label
                        htmlFor="End Date"
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
                        className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder=""
                      />
                      {error && enddate.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please select enddate
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                  </>
                )}
                <div className="mt-1">
                  <button
                    title="Click Search Button"
                    type="button"
                    onClick={viewTxnRequest}
                    data-modal-toggle="defaultModal"
                    className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-5 border border-blue-700 rounded  shadow-lg inner"
                  >
                    <MagnifyingGlassIcon className="h-3 w-3" />
                  </button>
                  <button
                    title="Clear Data"
                    type="button"
                    // onClick={handleClick}
                    className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-5 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-rose-700 rounded"
                  >
                    <XMarkIcon className="h-3 w-3" />
                  </button>
                </div>
              </div>
            </div>
          </div>
        </div>
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={Search}
        />

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={viewTxnRequest}
        /> */}
        <div className="overflow-x-auto relative shadow-md ">
          {/* {alldata.length > 0 && (
            <> */}
          <div className="table-wrp block max-h-[27rem] max-w-[29rem] ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
              <thead className="text-xs border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <th
                  scope="col"
                  className="py-2 px-14 whitespace-nowrap"
                  onClick={() => handleSort("txnId")}
                >
                  AMS Txn ID {renderSortArrow("txnId")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-14 whitespace-nowrap"
                  onClick={() => handleSort("txnRequest")}
                >
                  Txn Request {renderSortArrow("txnRequest")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-14 whitespace-nowrap"
                  onClick={() => handleSort("txnResponse")}
                >
                  Txn Response {renderSortArrow("txnResponse")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-14 whitespace-nowrap"
                  onClick={() => handleSort("reqTxnDate")}
                >
                  Request Txn Date {renderSortArrow("reqTxnDate")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-14 whitespace-nowrap"
                  onClick={() => handleSort("resTxnDate")}
                >
                  Response Txn Date {renderSortArrow("resTxnDate")}
                </th>
              </thead>
              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map((data) => (
                      <tr
                        key={uuidv4()}
                        className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                      >
                        <td className="px-14 py-2 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900">
                            {data.txnId || "N/A"}
                          </div>
                        </td>
                        <td className="text-sm text-gray-900 font-light px-14 py-2 whitespace-nowrap">
                          {data.txnRequest.cid}
                          <button
                            onClick={() =>
                              viewTxnContent(
                                data.reqTxnDate,
                                data.montraTxnId,
                                data.txnRequest
                              )
                            }
                          >
                            <EyeIcon className="h-5 text-green-600" />
                          </button>
                        </td>
                        <td className="text-sm text-gray-900 font-light px-14 py-2 whitespace-nowrap">
                          {data.txnResponse.cid}
                          <button
                            onClick={() =>
                              viewTxnResponsed(
                                data.code,
                                data.status,
                                data.message,
                                data.responseCode,
                                data.transactionDate,
                                data.authCode,
                                data.montraTxnId,
                                data.amsTransactionId,
                                data.agentAvailableBalance,
                                data.txnResponse
                              )
                            }
                          >
                            <EyeIcon className="h-5 text-green-600" />
                          </button>
                        </td>
                        <td className="px-14 py-2 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900 ">
                            {data.reqTxnDate || "N/A"}
                          </div>
                        </td>
                        <td className="px-14 py-2 whitespace-nowrap">
                          <div className="text-smclassName text-gray-900 ">
                            {data.resTxnDate || "N/A"}
                          </div>
                        </td>
                      </tr>
                    ))}
                  </>
                ) : (
                  <tr>
                    <td colSpan="12" className="px-14 py-1 text-center">
                      <span className="text-sm font-normal text-gray-500">
                        No Data Available.
                      </span>
                    </td>
                  </tr>
                )}
              </tbody>
            </table>
          </div>
          <Transition appear show={isShow} as={Fragment}>
            <Dialog
              as="div"
              className="relative z-10"
              onClose={closeProCategoryViewModel}
            >
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
              >
                <div className="fixed inset-0 bg-black bg-opacity-25" />
              </Transition.Child>
              <div className="fixed inset-0 overflow-y-auto">
                <div className="flex min-h-full items-center justify-center p-2 text-center">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                  >
                    <Dialog.Panel className="w-full max-w-md ring-1 transform overflow-hidden rounded-2xl bg-white p-3 text-left align-middle shadow-xl transition-all">
                      <DialogTitle>
                        <div
                          className="cursor-pointer absolute top-0 right-0 mt-2.5 mr-3 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out"
                          onClick={closeProCategoryViewModel}
                        >
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            aria-label="Close"
                            className="icon icon-tabler icon-tabler-x"
                            width={10}
                            height={10}
                            viewBox="0 0 24 24"
                            strokeWidth="2.5"
                            stroke="#dc2626"
                            fill="none"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          >
                            <path stroke="none" d="M0 0h24v24H0z" />
                            <line x1={18} y1={6} x2={6} y2={18} />
                            <line x1={6} y1={6} x2={18} y2={18} />
                          </svg>
                        </div>
                        <div className=" flex  justify-center bg-blue-300 rounded-lg py-1">
                          <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                            View Transaction Request
                          </p>
                        </div>
                      </DialogTitle>
                      <DialogContentText>
                        <span className="mx-1" key={uuidv4()}>
                          <div className="flex justify-center border-spacing-1 mb-1 border-amber-800 rounded-lg  ring-1 text-center break-all">
                            {" "}
                            {viewTxn}
                          </div>{" "}
                        </span>
                      </DialogContentText>
                    </Dialog.Panel>
                  </Transition.Child>
                </div>
              </div>
            </Dialog>
          </Transition>

          <Transition appear show={isShow1} as={Fragment}>
            <Dialog
              as="div"
              className="relative z-10"
              onClose={closeProCategoryViewModel1}
            >
              <Transition.Child
                as={Fragment}
                enter="ease-out duration-300"
                enterFrom="opacity-0"
                enterTo="opacity-100"
                leave="ease-in duration-200"
                leaveFrom="opacity-100"
                leaveTo="opacity-0"
              >
                <div className="fixed inset-0 bg-black bg-opacity-25" />
              </Transition.Child>

              <div className="fixed inset-0 overflow-hidden">
                <div className="flex min-h-full items-center justify-center p-4 text-center">
                  <Transition.Child
                    as={Fragment}
                    enter="ease-out duration-300"
                    enterFrom="opacity-0 scale-95"
                    enterTo="opacity-100 scale-100"
                    leave="ease-in duration-200"
                    leaveFrom="opacity-100 scale-100"
                    leaveTo="opacity-0 scale-95"
                  >
                    <Dialog.Panel className="w-auto transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                      <Dialog.Title
                        as="h3"
                        className="text-md font-medium leading-6 text-blue-900"
                      >
                        <div
                          className="cursor-pointer absolute top-0 right-0 mt-2.5 mr-2 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out"
                          onClick={closeProCategoryViewModel1}
                        >
                          <svg
                            xmlns="http://www.w3.org/2000/svg"
                            aria-label="Close"
                            className="icon icon-tabler icon-tabler-x"
                            width={10}
                            height={10}
                            viewBox="0 0 24 24"
                            strokeWidth="2.5"
                            stroke="#dc2626"
                            fill="none"
                            strokeLinecap="round"
                            strokeLinejoin="round"
                          >
                            <path stroke="none" d="M0 0h24v24H0z" />
                            <line x1={18} y1={6} x2={6} y2={18} />
                            <line x1={6} y1={6} x2={18} y2={18} />
                          </svg>
                        </div>
                        <div className=" flex  justify-center  bg-blue-300 rounded-lg py-1 ">
                          <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                            View Transaction Response
                          </p>
                        </div>
                      </Dialog.Title>

                      <DialogContentText>
                        <span className="mx-1" key={uuidv4()}>
                          <div className="flex  justify-center items-center border-spacing- mb-2 py-6 border-amber-800 rounded-lg  ring-1 text-center break-all">
                            {" "}
                            {viewTxndata}
                          </div>{" "}
                        </span>
                      </DialogContentText>
                    </Dialog.Panel>
                  </Transition.Child>
                </div>
              </div>
            </Dialog>
          </Transition>
        </div>
      </div>

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

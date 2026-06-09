import React, { useState, useEffect, useRef, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import swal from "sweetalert";
import { utils, write } from "xlsx";
import { v4 as uuidv4 } from "uuid";
import { useReactToPrint } from "react-to-print";
import Switch from "@mui/joy/Switch";
import ReactLoading from "react-loading";
import Typography from "@mui/joy/Typography";
import { Dialog, Transition } from "@headlessui/react";
import { XMarkIcon } from "@heroicons/react/24/solid";
import AutoPagintation from "../../UI/AutoPagintation";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintationReport from "./AutoPagintationReport";
const TxnReport = () => {
  const [alldataType, setAlldataType] = useState([]);
  const [alldata, setalldata] = useState([]);
  const [responseData, setResponseData] = useState([]);
  const [isLoadig, setisLoadig] = useState(false);
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [error, setError] = useState("");
    const [searchQuery, setSearchQuery] = useState("");
   const [itemPage, setItemPage] = useState(20);
   const [tcount, setTcount] = useState(1);
   const [error1, setError1] = useState("");
 
 
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [strResponseCode, setstrResponseCode] = useState("");
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
  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setTxntype("");
    setDuration("");
  };
  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);
  useEffect(() => {
    setDuration("");
  }, [checked]);
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
  // Dropdown Txn type

  const [txntype, setTxntype] = useState("");
  useEffect(() => {
    category();
  }, []);

  const category = async () => {
    try {
      const response = await amsApi.post(
        `txnReport/getAcountTxnType`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setAlldataType(response.data.txnReportlist);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // Dropdown Response Code
  useEffect(() => {
    responseCode();
  }, []);

  const responseCode = async () => {
    try {
      const response = await amsApi.get(`responseCodeMaster/getResponseCode`);
      if (response.data.code === "S0000") {
        setResponseData(response.data.responseCode);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      // handleShowError(error.response.data.message);
    }
  };
  // List

  const TotalCount = alldata.map((t) => [t.strTotalCount])[0];

  useEffect(() => {
    handleSubmit();
    setError("");
  }, [tcount, itemPage]);

  const handleSubmit = async (event) => {
    if (
      startdate === "" ||
      startdate.length === 0 ||
      enddate === "" ||
      enddate.length === 0 ||
      txntype === ""
      // ||
      // duration === ""
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      setisLoadig(true);
      try {
        const response = await amsApi.post(
          `txnReport/getTxnResponseStatment/pagination/${itemPage}/${tcount}`,

          {
            fromDate: startdate,
            strResponseCode: strResponseCode,
            toDate: enddate,
            strTxnTypeKeyWord: txntype,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setalldata(response.data.accountTranMaster);
          setChecked(false);
          setisLoadig(false);
          // sortDataByDate(); // Sort the data on initial fetch
        } else {
          handleShowError(response.data.message);
          setChecked(true);
          setalldata([]);
          setisLoadig(false);
        }
      } catch (error) {
        handleShowError(
          "Sorry, the server is under mentaines. Please try again later"
        );
        setalldata([]);
        setisLoadig(false);
      }
    }
  };

  
   useEffect(() => {
     if (searchQuery === "" || searchQuery === " ") {
       handleSubmit();
       setError("");
     }
   }, [searchQuery]);

   const Search = async () => {
     if (startdate === "" || enddate === "") {
       setError1(true);
     } else if (startdate > enddate) {
       swal("Please choose  Lesser Date from To Date");
     } else {
       try {
         const response = await amsApi.post(
           `txnReport/TxnReportSearch/pagination/20/1`,
           {
             keyword: searchQuery,
             fromDate: startdate,
             toDate: enddate,
           }
         );
         if (response.data.code === "S0000") {
           setalldata(response.data.accountTranMaster);
           setChecked(false);
           // handleShowSuccess(response.data.message);
         } else {
           handleShowError(response.data.message);
         }
       } catch (error) {
         // handleShowError(error.response.data.message);
       }
     }
   };

  // Download pdf and Excel
  const conponentPDF = useRef();
  const generatePDF = useReactToPrint({
    content: () => conponentPDF.current,
    documentTitle: "TableData",
  });

  const generateExcel = (data) => {
    const worksheet = utils.json_to_sheet(data);
    const workbook = { Sheets: { data: worksheet }, SheetNames: ["data"] };
    const excelBuffer = write(workbook, { bookType: "xlsx", type: "array" });
    const blob = new Blob([excelBuffer], {
      type: "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
    });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement("a");
    a.href = url;
    a.download = "Transaction Report.xlsx";
    a.click();
  };

  const [isOpen, setIsOpen] = useState(false);

  // Function to open the modal
  const openModal = () => {
    setIsOpen(true);
  };

  // Function to close the modal
  const closeModal = () => {
    setIsOpen(false);
  };

  const [alldata1, setAlldata1] = useState([]);

  // Open Model Function
  let [isOpen2, setIsOpen2] = useState(false);

  const openModal2 = () => {
    setIsOpen2(true);
  };
  // Function to close the modal
  const closeModal2 = () => {
    setIsOpen2(false);
  };

  const searchTransaction = async (strTxn_id) => {
    openModal2();
    // console.log(strTxn_id);
    // e.preventDefault();
    // if (transactionid === "" || transactionid.length === 0) {
    //   setError(true);
    // } else {
    try {
      const response = await amsApi.post(
        `accountTxnMaster/searchTransationByTxnId`,
        {
          strTxn_id: strTxn_id,
        },
        {
          headers: {
            "Content-type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setAlldata1(response.data.accountTranMaster);
        // openModal2();

        // handleShowSuccess(response.data.message);

        // setCheckpoint(1);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
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
    const dateA = new Date(a.txn_Date);
    const dateB = new Date(b.txn_Date);
    const timeA = new Date("1970/01/01 " + a.txn_Time);
    const timeB = new Date("1970/01/01 " + b.txn_Time);
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
  console.log(sortOrder);
  console.log(sortedData);

  // End
  const Tran_type = alldata1.map((t) => [t.strTran_type])[0];
  const Txn_id = alldata1.map((t) => [t.strTxn_id])[0];
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="px-9 sm:px-9 bg-blue-800 ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                Transaction Reports
              </p>

              {checked !== true && (
                <span className="flex justify-between ml-4">
                  <p class="px-4 flex ">
                    <span class="px-1 w-auto text-white text-sm">
                      Total_Count:
                    </span>
                    <span class="border border-black text-xs rounder-lg  w-14 black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                      {TotalCount}
                    </span>
                  </p>
                  <label
                    htmlFor="GL Account Description"
                    className="text-xs font-semibold text-white dark:text-white px-4"
                  >
                    From_Date
                    <span className="text-red-600 ">*</span>
                  </label>
                  <input
                    type="date"
                    id="account number "
                    value={startdate}
                    onChange={(e) => setStartdate(e.target.value)}
                    className="flex justify-between w-full px-3 py-0.5 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0  "
                    placeholder=""
                  />
                  {/* {error && startdate.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please select From Date
                    </p>
                  ) : (
                    ""
                  )} */}

                  <label
                    htmlFor="GL Account Description"
                    className="text-xs font-semibold ml-2 text-white dark:text-white"
                  >
                    To_Date
                    <span className="text-red-600 px-2">*</span>
                  </label>
                  <input
                    type="date"
                    value={enddate}
                    onChange={(e) => setEnddata(e.target.value)}
                    className="block w-full px-3 py-0.5 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    placeholder=""
                  />
                  {/* {error && enddate.length <= 0 ? (
                    <p className="text-red-500   text-xs font-medium">
                      Please select End Date
                    </p>
                  ) : (
                    ""
                  )} */}
                  <span className="flex px-3 ">
                    <button
                      title="Click Search Button"
                      type="submit"
                      onClick={handleSubmit}
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md  px-3 border border-blue-700 rounded  shadow-lg inner"
                    >
                      <MagnifyingGlassIcon className="h-3 w-4" />
                    </button>
                  </span>
                </span>
              )}
              {/* </p> */}
              {isLoadig && (
                <div className="flex items-center justify-center my-10">
                  <ReactLoading
                    type="balls"
                    color="#0143E2"
                    height={"10%"}
                    width={"8%"}
                  />
                </div>
              )}
              <div>
                <button className="inline-flex sm:ml-3  sm:mt-0 items-start justify-start  ml-8 focus:outline-none rounded text-xs font-medium">
                  <Switch
                    checked={checked}
                    onChange={(event) => setChecked(event.target.checked)}
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

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={alldata[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}

        {checked && (
          <div className=" md:col-span-2 lg:col-span-1  ">
            <div className="overflow-hidden shadow ">
              <div className=" px-2  sm:p-2 bg-white  ">
                <div className="grid gap-4  md:grid-cols-4  px-9">
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Transaction Type{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="accountType"
                      name="accountType"
                      value={txntype}
                      onChange={(e) => setTxntype(e.target.value)}
                      className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    >
                      <option value="">Select</option>
                      {[{ strTxnTypeKeyWord: "ALL" }, ...alldataType].map(
                        ({ strTxnTypeKeyWord }) => (
                          <option value={strTxnTypeKeyWord}>
                            {strTxnTypeKeyWord || "-"}
                          </option>
                        )
                      )}
                    </select>

                    {error && txntype.length <= 0 ? (
                      <p className="text-red-500   text-xs font-medium">
                        Please select Transaction Type
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  <div>
                    <label
                      htmlFor="text"
                      className="block text-xs font-semibold text-gray-700 dark:text-white"
                    >
                      Response Code{""}
                      <span className="text-red-600 px-2">*</span>
                    </label>
                    <select
                      id="strResponseCode"
                      name="strResponseCode"
                      value={strResponseCode}
                      onChange={(e) => setstrResponseCode(e.target.value)}
                      className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                    >
                      <option value="">Select</option>
                      <option value="ALL"> ALL</option>
                      <option value="00">Success</option>
                      <option value="Decline">Decline</option>

                      {[...responseData].map(({ strRespCode }) => (
                        <option value={strRespCode}>
                          {strRespCode || "-"}
                        </option>
                      ))}
                    </select>
                  </div>

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
                        Please select Duration
                      </p>
                    ) : (
                      ""
                    )}
                  </div>
                  {dateOpen && (
                    <>
                      <div>
                        <label
                          htmlFor="GL Account Description"
                          className="block text-xs font-semibold text-gray-700 dark:text-white"
                        >
                          Start Date
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="date"
                          id="account number "
                          value={startdate}
                          onChange={(e) => setStartdate(e.target.value)}
                          className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          placeholder=""
                        />
                        {error && startdate.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please select From Date
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                      <div>
                        <label
                          htmlFor="GL Account Description"
                          className="block text-xs font-semibold text-gray-700 dark:text-white"
                        >
                          End Date
                          <span className="text-red-600 px-2">*</span>
                        </label>
                        <input
                          type="date"
                          value={enddate}
                          onChange={(e) => setEnddata(e.target.value)}
                          className="block w-full px-3 py-1 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          placeholder=""
                        />
                        {error && enddate.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please select End Date
                          </p>
                        ) : (
                          ""
                        )}
                      </div>
                    </>
                  )}
                  <div className=" mt-1">
                    <button
                      title="Click Search Button"
                      type="submit"
                      onClick={handleSubmit}
                      data-modal-toggle="defaultModal"
                      className="bg-blue-500 hover:bg-blue-700 focus:ring-2 focus:ring-blue-800   focus:ring-offset-2 text-white font-md py-2 px-5 border border-blue-700 rounded  shadow-lg inner"
                    >
                      <MagnifyingGlassIcon className="h-2.5 w-3" />
                    </button>
                    <button
                      title="Clear Data"
                      type="button"
                      onClick={handleClick}
                      className="inline-flex justify-center bg-rose-500 hover:bg-rose-700 focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 py-2 px-5 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-rose-700 rounded"
                    >
                      <XMarkIcon className="h-2.5 w-3" />
                    </button>
                    <button
                      title="Go Back"
                      onClick={() => openModal()}
                      className="inline-flex justify-center bg-green-500 hover:bg-green-700 focus:ring-2 focus:ring-green-800 focus:ring-offset-2 py-2 px-5 mx-2 my-3 font-md  text-white  border  shadow-lg inner border-green-700 rounded"
                    >
                      <ArrowDownTrayIcon className="h-2.5 w-3" />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        )}

        {alldata.length > 0 && (
          <>
            <AutoPagintationReport
              addPageperData={setItemPage}
              addCurrentPage={setTcount}
              searchData={setSearchQuery}
              pagePerData={itemPage}
              currentPage={tcount}
              tcount={sortedData[0]?.strTotalCount}
              reCallApi={Search}
            />

            <div className="overflow-x-auto relative shadow-md mt-1 ">
              <div className="table-wrp block max-h-[33rem] max-w-[27rem]">
                <div ref={conponentPDF} style={{ width: "100%" }}>
                  <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                    <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strTxn_id")}
                      >
                        Txn id
                        {renderSortArrow("strTxn_id")}
                      </th>
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strTran_type")}
                      >
                        Txn type
                        {renderSortArrow("strTran_type")}
                      </th>

                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("txn_Date")}
                      >
                        {""}
                        Txn date
                        {renderSortArrow("txn_Date")}
                      </th>
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("txn_Time")}
                      >
                        Txn time
                        {renderSortArrow("txn_Time")}
                      </th>
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strTransaction_amount")}
                      >
                        Txn amount
                        {renderSortArrow("strTransaction_amount")}
                      </th>

                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strResponseCode")}
                      >
                        Txn resp code
                        {renderSortArrow("strResponseCode")}
                      </th>
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strAccountNumber")}
                      >
                        A/C
                        {renderSortArrow("strAccountNumber")}
                      </th>
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strFrom_account_number")}
                      >
                        From A/C no
                        {renderSortArrow("strFrom_account_number")}
                      </th>
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strTo_account_number")}
                      >
                        To A/C no
                        {renderSortArrow("strTo_account_number")}
                      </th>
                    </thead>
                    <tbody>
                      {sortedData.length > 0 ? (
                        <>
                          {sortedData.map(
                            ({
                              strTxn_id,
                              strTran_type,
                              txn_Date,
                              txn_Time,
                              strTransaction_amount,
                              strResponseCode,
                              strAccountNumber,
                              strFrom_account_number,
                              strTo_account_number,
                            }) => (
                              <tr
                                key={uuidv4()}
                                className="border-b dark:border-neutral-500"
                              >
                                <td className="whitespace-nowrap px-9 py-1">
                                  <button
                                    className="border-blue-300 shadow-md p-2  text-blue-700   rounded-full focus:ring-blue-400"
                                    onClick={() => searchTransaction(strTxn_id)}
                                  >
                                    {strTxn_id || "-"}

                                    {/* {strTxn_id || "-"} */}
                                  </button>
                                </td>
                                <td className="whitespace-nowrap px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {strTran_type || "-"}
                                  </span>
                                </td>
                                <td className="whitespace-nowrap px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {txn_Date || "-"}
                                  </span>
                                </td>

                                <td className="whitespace-nowrap px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {txn_Time || "-"}
                                  </span>
                                </td>

                                <td className="whitespace-nowrap  text-end px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {strTransaction_amount || "-"}
                                  </span>
                                </td>
                                <td className="whitespace-nowrap px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {strResponseCode || "-"}
                                  </span>
                                </td>
                                <td className="whitespace-nowrap px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {strAccountNumber || "-"}
                                  </span>
                                </td>
                                <td className="whitespace-nowrap px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {strFrom_account_number || "-"}
                                  </span>
                                </td>
                                <td className="whitespace-nowrap px-9 py-1">
                                  <span className="text-xs font-medium text-gray-900">
                                    {strTo_account_number || "-"}
                                  </span>
                                </td>
                              </tr>
                            )
                          )}
                        </>
                      ) : (
                        <tr>
                          <td colSpan="10" className="px-9 py-1 text-center">
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
          </>
        )}
      </div>

      {showAlert && (
        <CustomAlert
          title={alertTitle}
          message={alertMessage}
          type={alertType}
          onClose={handleCloseAlert}
        />
      )}
      {/* Delete model */}
      <Transition appear show={isOpen} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={() => {}}>
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
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-lg font-medium leading-6 text-gray-900"
                  >
                    Download
                  </Dialog.Title>
                  <div className="mt-2">
                    <p className="text-xs text-gray-500">
                      Are you sure you want to download file's ?
                    </p>
                  </div>
                  <div className=" px-9 py-1 sm:px-9 sm:flex sm:flex-row-reverse">
                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-9 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generatePDF(alldata)}
                    >
                      PDF
                    </button>
                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-9 py-2 bg-orange-600 text-base font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generateExcel(alldata)}
                    >
                      Excel
                    </button>
                    <button
                      type="button"
                      className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-9 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={closeModal}
                    >
                      Cancel
                    </button>
                  </div>
                </Dialog.Panel>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>

      {/*search Transaction  */}

      {/* Dilog box */}

      <Transition appear show={isOpen2} as={Fragment}>
        <Dialog as="div" className="relative z-10" onClose={() => {}}>
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
                <div className="table-wrp block max-h-[30rem]  w-full ">
                  <Dialog.Panel className="w-auto transform overflow-hidden rounded-2xl bg-white p-6 text-left align-middle shadow-xl transition-all">
                    <Dialog.Title
                      as="h3"
                      className="flex text-lg font-medium leading-6 text-black  justify-between bg-blue-300 px-4"
                    >
                      <p className="px-4">
                        <span className="px-1 text-black">
                          View Transaction
                        </span>
                      </p>
                      <p className="px-4 text-sm mt-1">
                        <span className="px-1 text-black">Tran Type:</span>
                        <span
                          style={{ wordWrap: "break-word" }}
                          className=" border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white "
                        >
                          {Tran_type}
                        </span>
                      </p>
                      <p className="px-4 text-sm mt-1">
                        <span className="px-1 text-black">Txn ID:</span>
                        <span className=" border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white ">
                          {Txn_id}
                        </span>
                      </p>
                    </Dialog.Title>
                    <div
                      className="cursor-pointer absolute top-0 right-0 mt-2.5 mr-3 border p-1 rounded-md shadow-md border-red-500 bg-red-200 transition duration-150 ease-in-out"
                      onClick={closeModal2}
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

                    <div className="overflow-x-auto  relative shadow-md  w-auto">
                      <div className="table-wrp block max-h-[27rem]   w-auto ">
                        <table class="shadow-md rounded  w-full m-auto ">
                          <thead class="sticky block top-0" scope="col">
                            <tr class="flex text-left ">
                              <th
                                scope="col"
                                class=" w-full p-1 border bg-white border-r-0 border-gray-300 font-normal"
                              >
                                {/* <h4 class="u-slab">Business</h4> */}
                                <p class=" mt-auto block text-white bg-indigo-500 text-md py-1 text-center rounded font-normal">
                                  Debit (From Account)
                                </p>
                              </th>
                              <th
                                scope="col"
                                class=" w-full p-1 border bg-white rounded-tr border-gray-300 font-normal"
                              >
                                <p class=" mt-auto block text-white bg-indigo-500 text-md py-1 text-center rounded font-normal">
                                  Credit (To Account)
                                </p>
                              </th>
                            </tr>
                          </thead>

                          {alldata1.map(
                            ({
                              strTxn_id,
                              txn_Date,
                              txn_Time,
                              strTran_type,
                              strTransaction_amount,
                              strAccountNumber,
                              strAccountType,
                              strFrom_account_number,
                              strTo_account_number,
                              strAuthCode,
                              strResponseCode,
                            }) => (
                              <tbody>
                                <tr class="flex text-left w-auto">
                                  <td class="bg-gray-100 border  flex justify-around sm:justify-around border-t-0  border-gray-300 w-full border-b-0 u-slab text-blue-500">
                                    {/* <div class="flex items-center px-2 flex-wrap sm:no-wrap justify-between sm:justify-start"> */}
                                    <p class="px-4">
                                      <span class="px-1 text-black text-sm ">
                                        Date:
                                      </span>
                                      <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                        {txn_Date}
                                      </span>
                                    </p>
                                    <p class="px-4">
                                      <span class="px-1 text-black text-sm">
                                        Time:
                                      </span>
                                      <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                        {txn_Time}
                                      </span>
                                    </p>
                                    <p class="px-4">
                                      <span class="px-1 text-black text-sm">
                                        Auth Code:
                                      </span>
                                      <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                        {strAuthCode}
                                      </span>
                                    </p>
                                    <p class="px-4">
                                      <span class="px-1 text-black text-sm">
                                        Response Code:
                                      </span>
                                      <span class="border border-black text-xs rounder-lg black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                                        {strResponseCode}
                                      </span>
                                    </p>
                                    {/* </div> */}
                                  </td>
                                </tr>

                                <tr class="flex text-left ">
                                  <th
                                    scope="col"
                                    class=" w-full  border bg-white rounded-tr border-gray-300 font-normal"
                                  >
                                    <div class="flex mb-2 mt-2 items-left  flex-wrap sm:no-wrap justify-between sm:justify-between">
                                      <ul class="flex justify-between   text-sm font-normal sm:block pl-1 ">
                                        <div className="flex p-1  space-x-2   ">
                                          <li class=" w-full px-3 py-1  text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap    ">
                                            Account Number
                                          </li>

                                          {strTran_type !== "WDL" && (
                                            <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                              {strFrom_account_number}
                                            </p>
                                          )}

                                          {strTran_type !== "PUR" && (
                                            <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                              {strFrom_account_number}
                                            </p>
                                          )}

                                          {strTran_type === "WDL" && (
                                            <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                              {strAccountNumber}
                                            </p>
                                          )}
                                          {strTran_type === "PUR" && (
                                            <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                              {strAccountNumber}
                                            </p>
                                          )}

                                          {/* <div className="flex p-1 "> */}
                                          <li class=" w-full px-3 py-1 pl-20 text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap  ">
                                            Transaction Amount
                                          </li>
                                          <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                            {strTransaction_amount}
                                          </p>
                                        </div>
                                        {/* </div> */}
                                      </ul>
                                      {/* <a
                                        href=""
                                        class="mt-auto block px-4 text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                        title=""
                                      >
                                        Total
                                      </a> */}
                                    </div>
                                  </th>
                                  {/* )} */}
                                  <th
                                    scope="col"
                                    class=" w-full  border bg-white rounded-tr border-gray-300 font-normal"
                                  >
                                    {strTran_type !== "TOT" && (
                                      <div class="flex mb-2 mt-2 items-left  flex-wrap sm:no-wrap justify-between sm:justify-between">
                                        <ul class="flex justify-between   text-sm font-normal sm:block pl-1 ">
                                          <div className="flex p-1  space-x-2   ">
                                            <li class=" w-full px-3 py-1  text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap    ">
                                              Account Number
                                            </li>
                                            <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                              {strTo_account_number}
                                            </p>

                                            {/* <div className="flex p-1 "> */}
                                            <li class=" w-full px-3 py-1 pl-20 text-gray-700 bg-white    u-slab text-sm font-bold whitespace-nowrap  ">
                                              Transaction Amount
                                            </li>
                                            <p className="w-full px-3 py-0.5 text-sm font-normal text-gray-700 bg-white ">
                                              {strTransaction_amount}
                                            </p>
                                          </div>
                                          {/* </div> */}
                                        </ul>
                                        {/* <a
                                        href=""
                                        class="mt-auto block px-4 text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                        title=""
                                      >
                                        Total
                                      </a> */}
                                      </div>
                                    )}
                                  </th>
                                </tr>
                              </tbody>
                            )
                          )}

                          {/* <tfoot>
                            <tr class="flex text-left text-sm">
                              <td class="w-1/4 hidden sm:block p-4 border-gray-300 border border-t-0 text-center bg-gray-100 border-r-0"></td>
                              <td class="w-1/3 sm:w-1/4 p-4 border-gray-300 border border-t-0 text-center border-r-0">
                                <a
                                  href=""
                                  class=" mt-auto block text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                  title=""
                                >
                                  Get Started
                                </a>
                              </td>
                              <td class="w-1/3 sm:w-1/4 p-4 border-gray-300 border border-t-0 text-center border-r-0">
                                <a
                                  href=""
                                  class=" mt-auto block text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal"
                                  title=""
                                >
                                  Request a demo
                                </a>
                              </td>
                              <td class="w-1/3 sm:w-1/4 p-4 border-gray-300 border border-t-0 text-center">
                                <a
                                  href=""
                                  class=" mt-auto block text-white bg-indigo-500 text-xs py-2 text-center rounded font-normal rounded-bt"
                                  title=""
                                >
                                  Request a demo
                                </a>
                              </td>
                            </tr>
                          </tfoot> */}
                        </table>
                      </div>
                    </div>
                  </Dialog.Panel>
                </div>
              </Transition.Child>
            </div>
          </div>
        </Dialog>
      </Transition>
    </AppLayout>
  );
};

export default TxnReport;

import React, { useState, useEffect, useRef, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import { Dialog, Transition } from "@headlessui/react";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { utils, write } from "xlsx";
import { useReactToPrint } from "react-to-print";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import swal from "sweetalert";
import { XMarkIcon } from "@heroicons/react/24/solid";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "./AutoPagintationReport";
export default function AccountStatement() {
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [accounttype, setAccountType] = useState("");
  const [type, setType] = useState([]);
  const [accountno, setAccountno] = useState("");
  const [alldata, setAlldata] = useState([]);
  let participantID = sessionStorage.getItem("Participantid");

  const [error, setError] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [checked, setChecked] = useState(true);
  const [dateOpen, setDateOpen] = useState(false);
  const [duration, setDuration] = useState("");
   const [searchQuery, setSearchQuery] = useState("");
   const [itemPage, setItemPage] = useState(20);
   const [tcount, setTcount] = useState(1);
   const [error1, setError1] = useState("");
 
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
    if (accountno.length > 9) {
      Holdername();
    }
  }, [accountno]);

  // Get Account Holder Name

  const Holdername = async () => {
    try {
      const response = await amsApi.post(`account/getAccountBalancAndName`, {
        strAccountNumber: accountno,
        strAccountType: accounttype,
      });

      if (response.data.code === "S0000") {
        setAlldata(response.data.accountInfoList);
      } else {
        // handleShowError("Account Number " + response.data.message);
      }
    } catch (error) {
      // handleShowError("Account Number " + error.response.data.message);
    }
  };

  // Dropdown Account Type

  useEffect(() => {
    Accounttype();
  }, []);

  const Accounttype = async () => {
    try {
      const response = await amsApi.post(
        `accountType/getACTypeListByParticiptWise`,

        {
          strParticipantId: participantID,
        },

        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setType(response.data.accountTypeMasterlistData);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  const [list, setList] = useState([]);
  useEffect(() => {
    handleSubmit();
    setError("")
  }, [tcount, itemPage]);

  const handleSubmit = async () => {
    // event.preventDefault();
    if (
      accountno === "" ||
      accountno.length === 0 ||
      accounttype === "" ||
      accounttype.length === 0 ||
      startdate === "" ||
      enddate === ""
    ) {
      setError(true);
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `account-statement/getAccountStatement/pagination/${itemPage}/${tcount}`,

          {
            strParticipantId: participantID,
            fromDate: startdate,
            toDate: enddate,
            strAccountType: accounttype,
            strAccountNumber: accountno,
          },

          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );

        if (response.data.code === "S0000") {
          setList(response.data.accountStatementList);
          setChecked(false);
          // showSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
          setList([]);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
        setChecked(true);
        setList([]);
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
           `account-statement/acctStatementSearch/pagination/${itemPage}/${tcount}`,
           {
             keyword: searchQuery,
             fromDate: startdate,
             toDate: enddate,
           }
         );
         if (response.data.code === "S0000") {
           setList(response.data.serachAcctStatementlist);
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

  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setAccountType("");
    setAccountno("");
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

    a.download = "account_statements.xlsx";

    a.click();
  };
  // Open Model Function
  let [isOpen, setIsOpen] = useState(false);
  function closeModal() {
    setIsOpen(false);
  }

  function openModal() {
    closeModal();

    setIsOpen(true);
  }

  // sorted table data by date and time

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
  const sortedData = list.slice(0).sort((a, b) => {
    const dateA = new Date(a.strTxnDate);
    const dateB = new Date(b.strTxnDate);
    const timeA = new Date("1970/01/01 " + a.strTxnTime);
    const timeB = new Date("1970/01/01 " + b.strTxnTime);
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

  //end

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-800 ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                Account Statements
              </p>
              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-8  focus:outline-none rounded text-xs font-normal">
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

        {checked && (
          <div className=" md:col-span-2 lg:col-span-1  ">
            <form className="">
              <div className="overflow-hidden shadow sm:rounded-md ">
                <div className=" px-4  bg-white ">
                  <div className="grid gap-4 lg:grid-cols-4 px-5">
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Account Type{" "}
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <select
                        id="accountType"
                        name="accountType"
                        value={accounttype}
                        onChange={(e) => setAccountType(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        {type.map((data) => (
                          <option value={data.strAccountType}>
                            {data.strAccountType}-{data.strDescription}
                          </option>
                        ))}
                      </select>
                      {error && accounttype.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please select Account Type
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
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
                        id="text"
                        autoComplete="off"
                        value={accountno}
                        // onChange={(e) => setAccountno(e.target.value)}
                        onChange={(e) => {
                          const value = e.target.value;
                          setAccountno(value);
                          const regex = /^[0-9]*$/;
                          if (!regex.test(value)) {
                            setErrorMessage(
                              " Only allow numeric digits"
                              // "Please enter only numeric values"
                            );
                          } else {
                            setErrorMessage("");
                          }

                          setError("");
                        }}
                        onBlur={() => {
                          if (accountno.length === 0) {
                            setError("Please enter Account Number!");

                            setErrorMessage("");
                          }
                        }}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        placeholder="Enter Account Number"
                      />

                      {errorMessage ? (
                        <p className="text-red-500 text-xs font-medium">
                          {errorMessage}
                        </p>
                      ) : error && accountno.length <= 0 ? (
                        <p className="text-red-500 text-xs font-medium">
                          Please enter Account Number!
                        </p>
                      ) : null}
                    </div>

                    {alldata.map((t) => (
                      <div>
                        <label
                          htmlFor="GL Account Description"
                          className="block text-xs font-semibold text-gray-700 dark:text-white"
                        >
                          Account Holder Name{""}
                          <span className="text-red-600 px-2">*</span>
                        </label>

                        <div>
                          <input
                            type="text"
                            id="account number "
                            value={t.strAccountHolderName}
                            onClick={Holdername}
                            autoComplete="off"
                            placeholder="Account Holder Name"
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                        </div>
                      </div>
                    ))}

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
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
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
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
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
                            id="account number "
                            value={enddate}
                            onChange={(e) => setEnddata(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                        </div>
                      </>
                    )}

                    <div className="">
                      <button
                        title="Click Search Button"
                        type="button"
                        onClick={handleSubmit}
                        data-modal-toggle="defaultModal"
                        className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5 mx-2  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                      >
                        <MagnifyingGlassIcon className="h-3 w-3" />
                      </button>

                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-5 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                      >
                        <XMarkIcon className="h-3 w-3" />
                      </button>

                      <button
                        title="Clcik to Download button"
                        onClick={() => openModal()}
                        className="inline-flex justify-center rounded-md border border-transparent bg-green-500 py-2 my-5 px-5 mx-2 text-xs font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
                      >
                        <ArrowDownTrayIcon className="h-3 w-3" />
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </form>
          </div>
        )}

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={list[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}
       
        {list.length > 0 && (
          <>
            <div className="grid gap-2 grid-cols-12 ">
              <div className="col-span-3 flex mt-2 px-2 ">
                <div className="px-4">
                  <label
                    htmlFor="startdate"
                    className="block text-xs px-2 font-semibold text-gray-700 dark:text-white"
                  >
                    Start Date
                  </label>

                  <input
                    type="date"
                    id="startdate"
                    value={startdate}
                    onChange={(e) => setStartdate(e.target.value)}
                    className="block w-32 px-3 py-0.5 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                  />

                  {error1 && startdate.length <= 0 ? (
                    <p className="text-red-500   text-xs font-sm">
                      Please Select Startdate
                    </p>
                  ) : (
                    ""
                  )}
                </div>
                <div>
                  <label
                    htmlFor="enddate"
                    className="block text-xs px-2 font-semibold text-gray-700 dark:text-white"
                  >
                    End Date
                  </label>

                  <input
                    type="date"
                    id="enddate"
                    value={enddate}
                    onChange={(e) => setEnddata(e.target.value)}
                    className="block w-32 px-3 py-0.5 text-xs font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                  />

                  {error1 && enddate.length <= 0 ? (
                    <p className="text-red-500   text-xs font-sm">
                      Please Select enddate
                    </p>
                  ) : (
                    ""
                  )}
                </div>
              </div>
              <div className="col-span-9 mt-4 px-2">
                <AutoPagintationReport
                  addPageperData={setItemPage}
                  addCurrentPage={setTcount}
                  searchData={setSearchQuery}
                  pagePerData={itemPage}
                  currentPage={tcount}
                  tcount={list[0]?.strTotalCount}
                  reCallApi={Search}
                />
              </div>
            </div>
            {/* table */}

            <div className="bg-white overflow-x-auto relative shadow-md ">
              <div className="table-wrp block max-h-[27rem] max-w-[60rem] ">
                <div ref={conponentPDF} style={{ width: "100%" }}>
                  <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                    <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strTxnDate")}
                      >
                        {""}
                        Txn date
                        {renderSortArrow("strTxnDate")}
                      </th>
                      <th
                        scope="col"
                        className="py-1 px-9 whitespace-nowrap"
                        onClick={() => handleSort("strTxnTime")}
                      >
                        Txn time
                        {renderSortArrow("strTxnTime")}
                      </th>

                      <th
                        scope="col"
                        className="py-1 px-8 whitespace-nowrap"
                        // onClick={() => handleSort("strTransactionID")}
                        onClick={() => handleSort("strTransactionID")}
                      >
                        Txn id {renderSortArrow("strTransactionID")}
                      </th>

                      <th
                        scope="col"
                        className="py-1 px-8 whitespace-nowrap"
                        onClick={() => handleSort("strTranType")}
                      // onClick={() => handleSort("strTranType")}
                      >
                        Txn Type{renderSortArrow("strTranType")}
                      </th>

                      <th
                        scope="col"
                        className="py-1 px-8 whitespace-nowrap"
                        onClick={() => handleSort("strTransactionAmount")}
                      >
                        Txn Amount {renderSortArrow("strTransactionAmount")}
                      </th>

                      <th
                        scope="col"
                        className="py-1 px-8 whitespace-nowrap"
                        onClick={() => handleSort("strClosingBalance")}
                      >
                        current balance {renderSortArrow("strClosingBalance")}
                      </th>

                      <th
                        scope="col"
                        className="py-1 px-8 whitespace-nowrap"
                        onClick={() => handleSort("strTransactionDetails")}
                      >
                        Txn Description{renderSortArrow("strTransactionDetails")}
                      </th>
                    </thead>

                    <tbody>
                      {sortedData.length > 0 ? (
                        <>
                          {sortedData.map((data) => (
                            <tr
                              key={uuidv4()}
                              className="border-b dark:border-neutral-500"
                            >
                              <td className="whitespace-nowrap px-8 py-1">
                                <span className="text-xs font-medium text-gray-900">
                                  {data.strTxnDate || "-"}
                                </span>
                              </td>
                              <td className="whitespace-nowrap px-8 py-1">
                                <span className="text-xs font-medium text-gray-900">
                                  {data.strTxnTime || "-"}
                                </span>
                              </td>

                              <td className="px-8 py-1 whitespace-nowrap">
                                <span className="text-xs font-medium text-gray-900">
                                  {data.strTransactionID || "-"}
                                </span>
                              </td>

                              <td className="px-8 py-1 whitespace-nowrap">
                                <span className="text-xs font-medium text-gray-900">
                                  {data.strTranType || "-"}
                                </span>
                              </td>

                              <td className="px-8 py-1 text-center whitespace-nowrap">
                                <span className="text-xs font-medium text-gray-900">
                                  {data.strTransactionAmount || "-"}
                                </span>
                              </td>

                              <td className="px-8 py-1 text-center whitespace-nowrap">
                                <span className="text-xs font-medium text-gray-900">
                                  {data.strClosingBalance || "-"}
                                </span>
                              </td>

                              <td className="px-8 py-1 whitespace-nowrap">
                                <span className="text-xs font-medium text-gray-900">
                                  {data.strTransactionDetails || "-"}
                                </span>
                              </td>
                            </tr>
                          ))}
                        </>
                      ) : (
                        <tr>
                          <td colSpan="6" className="px-8 py-1.5 text-center">
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
        <Dialog as="div" className="relative z-10" onClose={closeModal}>
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

                  <div className=" px-4 py-3 sm:px-8 sm:flex sm:flex-row-reverse">
                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-1.5 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={generatePDF}
                    >
                      PDF
                    </button>

                    <button
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-1.5 bg-orange-600 text-base font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generateExcel(list)}
                    >
                      Excel
                    </button>

                    <button
                      type="button"
                      className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-1.5 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-xs"
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
    </AppLayout>
  );
}

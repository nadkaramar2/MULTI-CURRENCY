import React, { useState, useEffect, useRef, Fragment } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import { utils, write } from "xlsx";
import { useReactToPrint } from "react-to-print";
import { Dialog, Transition } from "@headlessui/react";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import { XMarkIcon } from "@heroicons/react/24/solid";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "./AutoPagintationReport";
export default function GLaccountstmt() {
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [accounttype, setAccountType] = useState("");
  const [alldataType, setAlldataType] = useState([]);
  const [alldata, setallData] = useState([]);
  const [number, setNumber] = useState([]);
  const accountnumber = number.map((t) => [t.strAccountNumber]);
  sessionStorage.setItem("number", accountnumber);
  let Accountnumber = sessionStorage.getItem("number");
  const [error, setError] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [itemPage, setItemPage] = useState(20);
  const [tcount, setTcount] = useState(1);
  const [error1, setError1] = useState("");
  
 
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

  const [checked, setChecked] = useState(true);
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
  // Account type
  useEffect(() => {
    type();
  }, []);

  const type = async () => {
    try {
      const response = await amsApi.post(
        `gl-account-type/getGLAccountTypList`,
        {},
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setAlldataType(response.data.glAccountviewModels);
      } else {
        handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  // Get Account Number
  useEffect(() => {
    accountno();
    setError("");
  }, [accounttype]);
  const accountno = async () => {
    try {
      const response = await amsApi.post(`gl-account-type/getGLDescrption`, {
        strGLAccountType: accounttype,
      });
      if (response.data.code === "S0000") {
        setNumber(response.data.glAccountviewModels);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  // Save the Data

  useEffect(() => {
    handleSubmit();
    setError("");
  }, [tcount, itemPage]);

  const handleSubmit = async () => {
    if (
      startdate === "" ||
      startdate.length === 0 ||
      enddate === "" ||
      enddate.length === 0 ||
      accounttype === ""
    ) {
      setError(true);
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `gl-account-statement/getGLAcountStatementList/pagination/${itemPage}/${tcount}`,
          {
            fromDate: startdate,
            strAccountNumber: Accountnumber,
            strGLAccountType: accounttype,
            toDate: enddate,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setallData(response.data.gLAccountStatementList);
          // showSuccess(response.data.message);
        
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


  useEffect(() => {
    if (searchQuery === "" || searchQuery === " ") {
      handleSubmit();
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
          `gl-account-statement/glAcctStatementSearch/pagination/${itemPage}/${tcount}`,
          {
            keyword: searchQuery,
            fromDate: startdate,
            toDate: enddate,
          }
        );
        if (response.data.code === "S0000") {
          setallData(response.data.glAccountSearchList);
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
    setNumber([]);
    setError("");
    setDuration("");
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
    a.download = "gl_account_statements.xlsx";
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

  
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2">
          <div className="px-4 sm:px-4  bg-blue-800 ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                GL Account Statements
              </p>
              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-16 focus:outline-none rounded text-xs font-medium">
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
              <div className="overflow-hidden shadow ">
                <div className=" px-2 sm:p-2 bg-white ">
                  <div className="grid gap-2 md:grid-cols-3 lg:grid-cols-4 px-4">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700 dark:text-white"
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
                        {alldataType.map((data) => (
                          <option value={data.strGLAccountType}>
                            {data.strGLAccountType || "-"} -{""}
                            {data.strGLAccountDescription || "-"}
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
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                        i
                      >
                        Account Number{" "}
                        <span className="text-red-600 px-2">*</span>
                      </label>
                      <div>
                        <input
                          type="text"
                          id="number"
                          onClick={accountno}
                          value={Accountnumber}
                          placeholder="Enter  Account number"
                          autoComplete="off"
                          className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-gray-100 bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                        />
                      </div>
                    </div>

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

                    {/* </div>

            <div className="grid gap-6 mb-6 md:grid-cols-3 "> */}
                    {dateOpen && (
                      <>
                        {/* <div className="grid gap-6 mb-2 mt-2 md:grid-cols-2 lg:grid-cols-3 px-16"> */}
                        <div>
                          <label
                            htmlFor="GL Account Description"
                            className="block  text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Start Date
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
                            id="startdate"
                            value={startdate}
                            onChange={(e) => setStartdate(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
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
                            htmlFor="GL Account Description"
                            className="block  text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            End Date
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
                            id="enddate "
                            value={enddate}
                            onChange={(e) => setEnddata(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
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

                    <div className="">
                      <button
                        title="Click Search Button"
                        type="button"
                        onClick={handleSubmit}
                        data-modal-toggle="defaultModal"
                        className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1 px-5 mx-2 my-4 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                      >
                        <MagnifyingGlassIcon className="h-3 w-3" />
                      </button>
                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1 px-5 mx-2 my-4 text-xs font-medium text-white shadow-sm hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
                      >
                        <XMarkIcon className="h-3 w-3" />
                      </button>
                      <button
                        title="Go Back"
                        onClick={() => openModal()}
                        className="inline-flex justify-center rounded-md border border-transparent bg-green-500 py-1 px-5 mx-2 my-4 text-xs font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
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
              tcount={alldata[0]?.strTotalCount}
              reCallApi={Search}
            />
          </div>
        </div>

        {/* <AutoPagintation
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}

        {/* {data.length > 0 && (
          <> */}
        <div className="overflow-x-auto relative shadow-sm  ">
          <div className="table-wrp block max-h-[32rem]  max-w-[60rem]">
            <div ref={conponentPDF} style={{ width: "100%" }}>
              <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                  <th
                    scope="col"
                    className="py-1 px-16 whitespace-nowrap"
                    onClick={() => handleSort("strTransactionDate")}
                  >
                    TXN Date {renderSortArrow("strTransactionDate")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-16 whitespace-nowrap"
                    onClick={() => handleSort("strTxnId")}
                  >
                    TXN ID {renderSortArrow("strTxnId")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-16 whitespace-nowrap"
                    onClick={() => handleSort("strTranType")}
                  >
                    TXN Type {renderSortArrow("strTranType")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-16 text-end whitespace-nowrap"
                    onClick={() => handleSort("strAmount")}
                  >
                    TXN Amount {renderSortArrow("strAmount")}
                  </th>

                  <th
                    scope="col"
                    className="py-1 px-16 whitespace-nowrap"
                    onClick={() => handleSort("strTranMode")}
                  >
                    TXN Mode {renderSortArrow("strTranMode")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-16 whitespace-nowrap"
                    onClick={() => handleSort("strRef")}
                  >
                    Narration {renderSortArrow("strRef")}
                  </th>
                </thead>

                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map(
                        ({
                          strTransactionDate,
                          strTxnId,
                          strTranType,
                          strTranMode,
                          strAmount,
                          strRef,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="px-16 py-1 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {strTransactionDate || "-"}
                              </span>
                            </td>
                            <td className="px-16 py-1 whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {strTxnId || "-"}
                              </span>
                            </td>
                            <td className="px-16 py-1  whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {strTranType || "-"}
                              </span>
                            </td>
                            <td className="px-16 py-1 text-end whitespace-nowrap">
                              <span className="text-xs font-medium  text-gray-900">
                                {strAmount || "-"}
                              </span>
                            </td>
                            <td className="px-16 py-1  whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {strTranMode || "-"}
                              </span>
                            </td>
                            <td className="px-16 py-1  whitespace-nowrap">
                              <span className="text-xs font-medium text-gray-900">
                                {strRef || "-"}
                              </span>
                            </td>
                          </tr>
                        )
                      )}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="6" className="px-16 py-1.5 text-center">
                        <span className="text-sm font-normal text-gray-500">
                          No Data available.
                        </span>
                      </td>
                    </tr>
                  )}
                </tbody>
              </table>
            </div>
          </div>
        </div>
        {/* <MainPagination itemPageData={setItemPage} tCountData={setTcount} /> */}
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
                <Dialog.Panel className="w-full max-w-md transform overflow-hidden rounded-2xl bg-white p-5 text-left align-middle shadow-xl transition-all">
                  <Dialog.Title
                    as="h3"
                    className="text-lg font-normal leading-6 text-gray-900"
                  >
                    Download
                  </Dialog.Title>
                  <div className="mt-2">
                    <p className="text-xs text-gray-500">
                      Are you sure you want to download file's ?
                    </p>
                  </div>
                  <div className=" px-4 py-1 sm:px-16 sm:flex sm:flex-row-reverse">
                    <button
                      title="Clcik Here to Download Pdf"
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-1.5 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={generatePDF}
                    >
                      PDF
                    </button>
                    <button
                      title="Click Here to Download Excal"
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-1.5 bg-orange-600 text-base font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generateExcel(alldata)}
                    >
                      Excel
                    </button>
                    <button
                      title="Cancel Here"
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

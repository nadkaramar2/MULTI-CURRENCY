import React, { useState, useRef, Fragment, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import { utils, write } from "xlsx";
import { useReactToPrint } from "react-to-print";
import { Dialog, Transition } from "@headlessui/react";
import { XMarkIcon } from "@heroicons/react/24/solid";
import {
  ArrowDownTrayIcon,
  MagnifyingGlassIcon,
} from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";

import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "./AutoPagintationReport";
export default function JournalTxnReport() {
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [data, setdata] = useState([]);

  const [error, setError] = useState("");
  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [itemPage, setItemPage] = useState(20);
  const [tcount, setTcount] = useState(1);
    const [error1, setError1] = useState("");

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

  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setDuration("");
    setError("");
  };

  useEffect(() => {
    handleSubmit();
    setError("");
  }, [tcount, itemPage]);
  const handleSubmit = async (event) => {
    // event.preventDefault();
    if (
      startdate === "" ||
      startdate.length === 0 ||
      enddate === "" ||
      enddate.length === 0
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          // `txnReport/getJournalTxnReport`,
          `txnReport/getJournalTxnReport/pagination/${itemPage}/${tcount}`,
          {
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
          setdata(response.data.viewJournallist);
       
          setChecked(false);
        } else {
          handleShowError(response.data.message);
          setChecked(true);
          setdata([]);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
        setdata([]);
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
    if (
      startdate === "" ||
      enddate === ""
    ) {
      setError1(true);
    } else if (startdate > enddate) {
      swal("Please choose  Lesser Date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `journalTransfer/journalTxnSearch/pagination/20/1`,
          {
            keyword: searchQuery,
            fromDate: startdate,
            toDate: enddate,
          }
        );
        if (response.data.code === "S0000") {
          setdata(response.data.journalTransferList);
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
    a.download = "journal_Transactions.xlsx";
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

  // soreted table data

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

  const renderSortArrow = (column) => {
    if (column === sortColumn) {
      return sortOrder === "asc" ? "↑" : "↓";
    }
    return "↕";
  };

  // Sort the data based on the chosen column and order
  const sortedData = [...data].sort((a, b) => {
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
  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full ">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-800 ">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                Journal Transaction Report
              </p>

              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-4 focus:outline-none rounded text-xs font-medium">
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
                              sx={{ mr: "4px" }}
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
                <div className=" px-2  sm:p-2 bg-white ">
                  <div className="grid gap-6  md:grid-cols-4 px-8">
                    {/* <div className="grid gap-6 mb-6 md:grid-cols-3 "> */}

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
                            className="block  text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Start Date
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
                            value={startdate}
                            onChange={(e) => setStartdate(e.target.value)}
                            className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
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
                            className="block  text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            End Date
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="date"
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
                        className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5 mx-2 my-5 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                      >
                        <MagnifyingGlassIcon className="h-3 w-3" />
                      </button>
                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-5 mx-2 my-5 text-xs font-medium text-white shadow-sm hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
                      >
                        <XMarkIcon className="h-3 w-3" />
                      </button>
                      <button
                        title="Go Back"
                        onClick={() => openModal()}
                        className="inline-flex justify-center rounded-md border border-transparent bg-green-500 py-2 px-5 mx-2 my-5 text-xs font-medium text-white shadow-sm hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-green-500 focus:ring-offset-2"
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
          tcount={filteredData[0]?.strTotalCount}
          reCallApi={handleSubmit}
        /> */}

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
              tcount={data[0]?.strTotalCount}
              reCallApi={Search}
            />
          </div>
        </div>
        <div className="overflow-x-auto relative shadow-md sm:rounded-lg ">
          <div className="table-wrp block max-h-[27rem]  max-w-[27rem] ">
            <div ref={conponentPDF} style={{ width: "100%" }}>
              <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strTxnDte")}
                  >
                    Txn date {renderSortArrow("strTxnDte")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strTxnTime")}
                  >
                    Txn time {renderSortArrow("strTxnTime")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strTxnId")}
                  >
                    Txn id {renderSortArrow("strTxnId")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strFromAccountType")}
                  >
                    From A/C type {renderSortArrow("strFromAccountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strFromAccountNumber")}
                  >
                    From A/C number {renderSortArrow("strFromAccountNumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strFromAccountName")}
                  >
                    From A/C name {renderSortArrow("strFromAccountName")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strToAccountType")}
                  >
                    To A/C type {renderSortArrow("strToAccountType")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strToAccountNumber")}
                  >
                    To A/C number {renderSortArrow("strToAccountNumber")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strToAccountName")}
                  >
                    To A/C name {renderSortArrow("strToAccountName")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strAmoutToTransfer")}
                  >
                    Amount to transfer {renderSortArrow("strAmoutToTransfer")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strNarration")}
                  >
                    Narration {renderSortArrow("strNarration")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strTxnStatus")}
                  >
                    Status {renderSortArrow("strTxnStatus")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strMakerId")}
                  >
                    Maker user id {renderSortArrow("strMakerId")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strCheckerId")}
                  >
                    Checker user id {renderSortArrow("strCheckerId")}
                  </th>
                </thead>

                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map((value) => (
                        <tr
                          key={uuidv4()}
                          className=" border-b dark:border-neutral-500"
                        >
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strTxnDte || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strTxnTime || "-"}
                            </div>
                          </td>

                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strTxnId || "-"}
                            </div>
                          </td>

                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strFromAccountType || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strFromAccountNumber || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strFromAccountName || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strToAccountType || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strToAccountNumber || "_ "}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strToAccountName || "_ "}
                            </div>
                          </td>
                          <td className="px-4 py-1 text-end whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strAmoutToTransfer || "_ "}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strNarration || "_ "}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strTxnStatus || "_ "}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strMakerId || "_ "}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {value.strCheckerId || "_ "}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="14" className="px-4 py-1.5 text-center">
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
                  <div className=" px-4 py-3 sm:px-4 sm:flex sm:flex-row-reverse">
                    <button
                      title="Clcik Here to Download pdf"
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-green-600 text-base font-medium text-white hover:bg-green-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={generatePDF}
                    >
                      PDF
                    </button>
                    <button
                      title="Click Here to Download Excal"
                      type="button"
                      className="w-full inline-flex justify-center rounded-md border border-transparent shadow-sm px-4 py-2 bg-orange-600 text-base font-medium text-white hover:bg-orange-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-green-500 sm:ml-3 sm:w-auto sm:text-xs"
                      onClick={() => generateExcel(data)}
                    >
                      Excel
                    </button>
                    <button
                      title="Cancel Here"
                      type="button"
                      className="mt-3 w-full inline-flex justify-center rounded-md border border-gray-300 shadow-sm px-4 py-2 bg-white text-base font-medium text-gray-700 hover:bg-gray-50 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-blue-500 sm:mt-0 sm:ml-3 sm:w-auto sm:text-xs"
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

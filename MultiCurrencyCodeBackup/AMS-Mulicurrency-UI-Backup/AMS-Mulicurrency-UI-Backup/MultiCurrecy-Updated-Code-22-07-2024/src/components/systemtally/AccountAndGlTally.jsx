import React, { useState, useEffect, useRef } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import { useReactToPrint } from "react-to-print";
import Swal from "sweetalert2";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
export default function AccountAndGlTally() {
  const [showScreen, setShowScreen] = useState({
    searchDetails: true,
    viewOptions: false,
    viewSummery: false,
    viewDetails: false,
  });
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [todayDate, setTodayDate] = useState("");
  const [error, setError] = useState("");

  useEffect(() => {
    let currDate = new Date().toLocaleDateString("en-CA");
    setTodayDate(currDate);
  }, []);

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

  const showSuccess = (resMessage) => {
    Swal.fire({
      title: "Success",
      text: resMessage,
      allowOutsideClick: false,
      icon: "success",
      confirmButtonText: "OK",
    });
  };
  const showError = (resMessage) => {
    Swal.fire({
      text: resMessage,
      allowOutsideClick: false,
      icon: "error",
      title: "Oops...",
      confirmButtonText: "OK",
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    if (
      startDate === "" ||
      startDate.length === 0 ||
      endDate === "" ||
      endDate.length === 0
    ) {
      setError(true);
      //  swal("please enter a valid Input (non-empty values).");
    } else {
      setShowScreen((prevState) => ({ ...prevState, viewOptions: true }));
    }
  };

  const viewSummery = () => {
    viewSummeryRequest();
  };
  const viewDetails = () => {
    viewDetailsRequest();
  };

  const clearInputData = () => {
    setStartDate("");
    setEndDate("");
    setShowScreen((prevState) => ({ ...prevState, viewOptions: false }));
    setShowScreen((prevState) => ({ ...prevState, viewSummery: false }));
    setShowScreen((prevState) => ({ ...prevState, viewDetails: false }));
    setDuration("");
  };
  const accountDataList = [
    {
      label: "PPD-Prepaid Account Type",
      noCountDebit: "50",
      amtDebit: "50,000",
      noCountCredit: "50",
      amtCredit: "50,000",
    },
    {
      label: "AGT-Agent Account Type",
      noCountDebit: "10",
      amtDebit: "10,000",
      noCountCredit: "10",
      amtCredit: "10,000",
    },
    {
      label: "MAT-Merchant Account Type",
      noCountDebit: "40",
      amtDebit: "40,000",
      noCountCredit: "40",
      amtCredit: "40,000",
    },
  ];
  const viewDeatilsList = [
    {
      txnId: "20100001",
      txnDate: "2023-02-01",
      txnTime: "20:22:22",
      debitAccType: "PPD",
      debitAccNo: "200092221",
      debitAmount: "22,000",
      creditAccType: "PPD",
      creditAccNo: "200092201",
      creditAmount: "22,000",
    },
    {
      txnId: "20100002",
      txnDate: "2023-02-03",
      txnTime: "20:22:22",
      debitAccType: "PPD",
      debitAccNo: "200092221",
      debitAmount: "22,000",
      creditAccType: "PPD",
      creditAccNo: "200092201",
      creditAmount: "22,000",
    },
  ];
  const [viewSummeryResData, setViewSummeryResData] = useState({});
  const [viewDetailsResData, setViewDetailsResData] = useState({});

  const viewSummeryRequest = async () => {
    poolAccountRequest();
    try {
      const response = await amsApi.post(
        `systemtally/summarydetails`,
        {
          startDate: startDate,
          endDate: endDate,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (response.data.code === "S0000") {
        setViewSummeryResData(response.data.summaryModel);
        setError("");
        setShowScreen((prevState) => ({ ...prevState, viewDetails: false }));
        setShowScreen((prevState) => ({ ...prevState, viewSummery: true }));
      } else {
        clearInputData();
        setError("");
        handleShowError(response.data.message);
      }
    } catch (error) {
      clearInputData();
      setError("");
      handleShowError(error.response.data.message);
    }
  };
  const viewDetailsRequest = async () => {
    try {
      const response = await amsApi.post(
        `systemtally/details`,
        {
          startDate: startDate,

          endDate: endDate,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        //  showSuccess(response.data.message);
        setViewDetailsResData(response.data.tallyUntallyDetails);
        setError("");
        setShowScreen((prevState) => ({ ...prevState, viewSummery: false }));
        setShowScreen((prevState) => ({ ...prevState, viewDetails: true }));
      } else {
        clearInputData();
        setError("");
        handleShowError(response.data.message);
      }
    } catch (error) {
      clearInputData();
      setError("");
      handleShowError(error.response.data.message);
    }
  };

  // Download pdf
  const conponentPDF = useRef();
  const generatePDF = useReactToPrint({
    content: () => conponentPDF.current,
    documentTitle: "Userdata",
    onAfterPrint: () => handleShowSuccess("Data saved in PDF"),
  });

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
      setStartDate(date);
      setEndDate(date);
    } else if (selectData === "1") {
      remainDays(1);
      setStartDate(formatDate(yesterday));
      setEndDate(formatDate(yesterday));
    } else if (selectData === "7") {
      remainDays(7);
      setStartDate(formatDate(yesterday));
      setEndDate(date);
    } else if (selectData === "10") {
      remainDays(10);
      setStartDate(formatDate(yesterday));
      setEndDate(date);
    } else if (selectData === "custom") {
      setDateOpen(true);
    } else if (selectData === "cMonth") {
      remainDays(parseInt(daysInCurrentMonth) - 1);
      setStartDate(formatDate(yesterday));
      setEndDate(date);
    } else if (selectData === "pMonth") {
      setStartDate(formatDate(startOfMonth(current)));
      setEndDate(formatDate(endOfMonth(current)));
    }
  };
  const [poolAccBal, setPoolAccBal] = useState("");
  const poolAccountRequest = async () => {
    try {
      const response = await amsApi.get(
        `thirdParty/NGN/middleWare/getPoolAccountInfo`
      );

      if (response.data.code === "S0000") {
        setPoolAccBal(response.data.poolAccountBalance);
      } else {
        // handleShowError(response.data.message);
      }
    } catch (error) {
      handleShowError(error.response.data.message);
    }
  };

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full sticky top-0">
        <div className="w-full shadow-md mt-2 bg-blue-200">
          <div className="px-8 sm:px-8 rounded-t-lg ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                Account And GL Tally
              </p>
            </div>
          </div>
        </div>
        <div className="max-w-full mx-auto h-full overflow-auto max-h-full lg:max-h-[32rem] ">
          {showScreen.searchDetails && (
            <>
              <div className=" md:col-span-2 lg:col-span-1  bg-blue-200">
                <form className="" onSubmit={handleSubmit}>
                  <div className="overflow-hidden shadow sm:rounded-md ">
                    <div className=" px-8 py-1 sm:p-2 bg-white">
                      <div className="grid gap-6  md:grid-cols-4 px-8">
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
                        </div>

                        {dateOpen && (
                          <>
                            <div>
                              <label
                                htmlFor="GL Account Description"
                                className="block text-xs font-semibold text-gray-700"
                              >
                                From Date
                                <span className="text-red-600 px-2">*</span>
                              </label>
                              <input
                                type="date"
                                id="startDate"
                                value={startDate}
                                onChange={(e) => setStartDate(e.target.value)}
                                className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                                max={todayDate}
                              />
                              {error && startDate.length <= 0 ? (
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
                                className="block text-xs font-semibold text-gray-700"
                              >
                                To Date
                                <span className="text-red-600 px-2">*</span>
                              </label>
                              <input
                                type="date"
                                id="endDate"
                                value={endDate}
                                onChange={(e) => setEndDate(e.target.value)}
                                className="block w-full px-3 py-1 text-base font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                                min={startDate}
                                max={todayDate}
                              />
                              {error && endDate.length <= 0 ? (
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
                            type="submit"
                            data-modal-toggle="defaultModal"
                            className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-2 px-5 mx-2 my-5 text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                          >
                            <MagnifyingGlassIcon className="h-2.5 w-3" />
                          </button>
                          <button
                            title="Clear Data"
                            type="button"
                            onClick={clearInputData}
                            className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-2 px-5 mx-2 my-5 text-xs font-medium text-white shadow-sm hover:bg-rose-700 focus:outline-none focus:ring-2 focus:ring-rose-500 focus:ring-offset-2"
                          >
                            <XMarkIcon className="h-2.5 w-3" />
                          </button>
                        </div>
                      </div>
                    </div>
                  </div>
                </form>
              </div>
            </>
          )}
          {showScreen.viewOptions && (
            <>
              <div className=" md:col-span-2 lg:col-span-1  bg-blue-200 mt-1">
                <div ref={conponentPDF} style={{ width: "100%" }}>
                  <div className="overflow-hidden shadow sm:rounded-md ">
                    <div className="bg-gray-100 px-8 py-2  sm:px-2 ">
                      <button
                        type="submit"
                        onClick={viewSummery}
                        className="inline-flex justify-center rounded-md border border-transparent bg-blue-500 py-1.5 px-3  text-xs font-medium text-white shadow-sm hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 focus:ring-offset-2"
                      >
                        View Summary
                      </button>
                      <button
                        type="button"
                        onClick={viewDetails}
                        className="inline-flex justify-center rounded-md border border-transparent bg-rose-500 py-1.5 px-5 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                      >
                        View Details
                      </button>
                      <button
                        type="button"
                        onClick={() => alert("Downloading....")}
                        className="inline-flex justify-center rounded-md border border-transparent bg-green-500 py-1.5 px-5 mx-2 text-xs font-medium text-white shadow-sm hover:bg-gray-700 focus:outline-none focus:ring-2 focus:ring-gray-500 focus:ring-offset-2"
                      >
                        PDF
                      </button>
                    </div>
                  </div>
                </div>
              </div>
            </>
          )}
          {showScreen.viewSummery && (
            <>
              {/* table Account */}

              <div className="relative overflow-x-auto shadow-md  sm:rounded-lg mt-1 max-h-full lg:max-h-[27rem] ">
                <div ref={conponentPDF} style={{ width: "100%" }}>
                  <table className="w-full text-xs text-left text-gray-500 dark:text-gray-400">
                    <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                      <tr>
                        <th
                          scope="row"
                          colSpan={5}
                          className="px-8 py-3 text-white   bg-blue-400 dark:bg-gray-800"
                        >
                          Data for the period {viewSummeryResData.startDate} to
                          {""}
                          {viewSummeryResData.endDate}
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="col"
                          className="px-8 py-3  bg-blue-200 dark:bg-gray-800"
                        >
                          Control Account Balance
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2 bg-gray-100 text-right"
                        >
                          {viewSummeryResData.controlAccountBlance}
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Pool Account Balance
                        </th>
                        <th scope="col" className="px-8 py-2 bg-gray-100"></th>
                        <th
                          scope="col"
                          className="px-8 py-2 bg-gray-100 text-right"
                        >
                          {
                            //viewSummeryResData.poolAccountBlance
                            poolAccBal
                          }
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="row"
                          colSpan={5}
                          className="px-8 py-2  text-center bg-blue-100 dark:bg-gray-800"
                        >
                          Account Type wise Details
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        ></th>
                        <th
                          scope="col"
                          colSpan={2}
                          className="px-8 py-3 text-center bg-blue-200 dark:bg-gray-800"
                        >
                          Debit
                        </th>

                        <th
                          scope="col"
                          colSpan={2}
                          className="px-8 py-2  text-center bg-blue-200 dark:bg-gray-800"
                        >
                          Credit
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Individual Account Type
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Count of successful transaction
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Amount of successful transactions
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Count of successful transaction
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Amount of successful transactions
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <>
                        {viewSummeryResData.accountTypeWiseList.tallydetails.map(
                          (data) => (
                            <tr
                              key={uuidv4()}
                              className="border-b dark:border-neutral-500"
                            >
                              <td className="whitespace-nowrap px-8  py-2 ">
                                <span className="text-sm font-medium text-gray-900 ">
                                  {data.accountType || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.debitNoOfTxn || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap text-right">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.debitNoOfTxnAmt || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.creditNoOfTxn || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap text-right">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.creditNoOfTxnAmt || "-"}
                                </span>
                              </td>
                            </tr>
                          )
                        )}
                      </>
                    </tbody>
                    <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                      <tr>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2 	 bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.accountTypeWiseList
                              .totalDebitNoOfTxn
                          }
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2 text-right	 bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.accountTypeWiseList
                              .totalDebitNoOfTxnAmt
                          }
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.accountTypeWiseList
                              .totalCreditNoOfTxn
                          }
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2 text-right bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.accountTypeWiseList
                              .totalCreditNoOfTxnAmt
                          }
                        </th>
                      </tr>
                    </thead>
                  </table>
                </div>
              </div>
              {/* table GL */}

              <div className="relative overflow-x-auto shadow-md sm:rounded-lg mt-1">
                <div ref={conponentPDF} style={{ width: "100%" }}>
                  <table className="w-full text-xs text-left text-gray-500 dark:text-gray-400">
                    <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                      <tr>
                        <th
                          scope="row"
                          colSpan={5}
                          className="px-8 py-2 text-white  bg-blue-400 dark:bg-gray-800"
                        >
                          Tallied Data for the period{""}
                          {viewSummeryResData.startDate} to{""}
                          {viewSummeryResData.endDate}
                        </th>
                      </tr>

                      <tr>
                        <th
                          scope="row"
                          colSpan={5}
                          className="px-8 py-2  text-center bg-blue-100 dark:bg-gray-800"
                        >
                          General Ledger wise details
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        ></th>
                        <th
                          scope="col"
                          colSpan={2}
                          className="px-8 py-2 text-center bg-blue-200 dark:bg-gray-800"
                        >
                          Debit
                        </th>
                        <th
                          scope="col"
                          colSpan={2}
                          className="px-8 py-2  text-center bg-blue-200 dark:bg-gray-800"
                        >
                          Credit
                        </th>
                      </tr>
                      <tr>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Individual GL Account Type
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Count of successful transaction
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Amount of successful transactions
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Count of successful transaction
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          Total Amount of successful transactions
                        </th>
                      </tr>
                    </thead>
                    <tbody>
                      <>
                        {viewSummeryResData.glAccountTypeWiseList.tallydetails.map(
                          (data) => (
                            <tr
                              key={uuidv4()}
                              className="border-b dark:border-neutral-500"
                            >
                              <td className=" px-8 py-2">
                                <span className="text-sm font-medium text-gray-900 break-all">
                                  {data.accountType || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.debitNoOfTxn || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap text-right">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.debitNoOfTxnAmt || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.creditNoOfTxn || "-"}
                                </span>
                              </td>
                              <td className="px-8  py-2 whitespace-nowrap text-right">
                                <span className="text-sm font-medium text-gray-900">
                                  {data.creditNoOfTxnAmt || "-"}
                                </span>
                              </td>
                            </tr>
                          )
                        )}
                      </>
                    </tbody>
                    <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                      <tr>
                        <th
                          scope="col"
                          className="px-8 py-3  bg-blue-200 dark:bg-gray-800"
                        >
                          Total
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2 	 bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.glAccountTypeWiseList
                              .totalDebitNoOfTxn
                          }
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2 text-right	 bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.glAccountTypeWiseList
                              .totalDebitNoOfTxnAmt
                          }
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.glAccountTypeWiseList
                              .totalCreditNoOfTxn
                          }
                        </th>
                        <th
                          scope="col"
                          className="px-8 py-2 text-right bg-blue-200 dark:bg-gray-800"
                        >
                          {
                            viewSummeryResData.glAccountTypeWiseList
                              .totalCreditNoOfTxnAmt
                          }
                        </th>
                      </tr>
                    </thead>
                  </table>
                </div>
              </div>

              {/* table Grand Total (Account Type Total + General Ledger Total) */}
              <div className="relative overflow-x-auto shadow-md sm:rounded-lg mt-3 mb-5">
                <table className="w-full text-xs text-left text-gray-500 dark:text-gray-400">
                  <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                    <tr>
                      <th
                        scope="col"
                        className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Total Debits
                      </th>
                      <th
                        scope="col"
                        className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Total Credit
                      </th>
                      <th
                        scope="col"
                        className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Status
                      </th>
                      <th
                        scope="col"
                        className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Action
                      </th>
                    </tr>
                  </thead>

                  <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                    <tr>
                      <th
                        scope="col"
                        className="px-8 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        {viewSummeryResData.totalDebit}
                      </th>
                      <th
                        scope="col"
                        className="px-8 py-2 	 bg-blue-200 dark:bg-gray-800"
                      >
                        {viewSummeryResData.totalCredit}
                      </th>
                      <th
                        scope="col"
                        className="px-8 py-2 	 bg-blue-200 dark:bg-gray-800"
                      >
                        {viewSummeryResData.status}
                      </th>
                      <th
                        scope="col"
                        className="px-8 py-2 	 bg-blue-200 dark:bg-gray-800"
                      >
                        {/* <a href="#" className="font-medium text-blue-600 dark:text-blue-500 hover:underline">View</a> */}
                        <button
                          type="button"
                          className="font-medium text-blue-600 dark:text-blue-500 hover:underline"
                          onClick={() => viewDetails()}
                        >
                          view details
                        </button>
                      </th>
                    </tr>
                  </thead>
                </table>
              </div>
            </>
          )}
          {showScreen.viewDetails && (
            <>
              {/* table tallied or untallied details*/}
              <div className="relative overflow-x-auto shadow-md sm:rounded-lg mt-2 mb-5 max-h-[23rem]  max-w-full mr-8">
                <table className="w-full text-xs text-left text-gray-500 dark:text-gray-400">
                  <thead className="text-xs text-gray-700 uppercase dark:text-gray-400">
                    <tr>
                      <th
                        scope="row"
                        colSpan={9}
                        className="px-12 py-2 text-white   bg-blue-400 dark:bg-gray-800"
                      >
                        {"Tallied/Un-Tallied"} Data for the period {startDate}
                        {""}
                        to {endDate}
                      </th>
                    </tr>
                    <tr>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      ></th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      ></th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      ></th>
                      <th
                        scope="col"
                        colSpan={3}
                        className="px-12 py-2 text-center bg-blue-200 dark:bg-gray-800"
                      >
                        Debit
                      </th>

                      <th
                        scope="col"
                        colSpan={3}
                        className="px-12 py-2  text-center bg-blue-200 dark:bg-gray-800"
                      >
                        Credit
                      </th>
                    </tr>

                    <tr>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Transaction ID
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Date of txn
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800 "
                      >
                        Time of txn
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Account Type
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Account Number
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Amount
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Account Type
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Account Number
                      </th>
                      <th
                        scope="col"
                        className="px-12 py-2  bg-blue-200 dark:bg-gray-800"
                      >
                        Amount
                      </th>
                    </tr>
                  </thead>

                  <tbody>
                    <>
                      {viewDetailsResData.map((data) => (
                        <tr
                          key={uuidv4()}
                          className="border-b dark:border-neutral-500"
                        >
                          <td className="whitespace-nowrap px-12  py-2">
                            <span className="text-sm font-medium text-gray-900">
                              {data.txnid || "-"}
                            </span>
                          </td>
                          <td className="px-12  py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {data.dateoftxn || "-"}
                            </span>
                          </td>
                          <td className="px-12  py-2 whitespace-nowrap ">
                            <span className="text-sm font-medium text-gray-900">
                              {data.timeoftxn || "-"}
                            </span>
                          </td>
                          <td className="px-12 py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {data.debitaccounttype || "-"}
                            </span>
                          </td>
                          <td className="px-12  py-2 whitespace-nowrap ">
                            <span className="text-sm font-medium text-gray-900">
                              {data.debitaccountno || "-"}
                            </span>
                          </td>

                          <td className="px-12  py-2 whitespace-nowrap text-right">
                            <span className="text-sm font-medium text-gray-900">
                              {data.debitamount || "-"}
                            </span>
                          </td>
                          <td className="px-12  py-2 whitespace-nowrap">
                            <span className="text-sm font-medium text-gray-900">
                              {data.creditaccounttype || "-"}
                            </span>
                          </td>
                          <td className="px-12  py-2 whitespace-nowrap ">
                            <span className="text-sm font-medium text-gray-900">
                              {data.creditaccountno || "-"}
                            </span>
                          </td>
                          <td className="px-12  py-2 whitespace-nowrap text-right">
                            <span className="text-sm font-medium text-gray-900">
                              {data.creditamount || "-"}
                            </span>
                          </td>
                        </tr>
                      ))}
                    </>
                  </tbody>
                </table>
              </div>
            </>
          )}
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

import React, { useState, useEffect } from "react";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import { v4 as uuidv4 } from "uuid";
export default function Viewrevolvingcreditcard() {
  const [checked, setChecked] = useState(true);
  const [alldata, setalldata] = useState([]);
  const [isLoadig, setisLoadig] = useState(false);
  const [dateOpen, setDateOpen] = useState(false);
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(1);
  const [accnumberOpen, setAccnumberOpenOpen] = useState(false);
  const [Accountnoholder, setAccountnoHandler] = useState("");
  const [duration, setDuration] = useState("");
  const [error, setError] = useState("");
  const [accountno, setAccountno] = useState("");
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);

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
    setAccountno("");
    setStartdate("");
    setEnddata("");
    setDuration("");
    setError("");
    setAccountnoHandler("");
    setNameData([]);
  };

  const [showAlert, setShowAlert] = useState(false);
  const [alertType, setAlertType] = useState("");
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");

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

  const handleSubmit = (e) => {
    if (Accountnoholder === "ALL") {
      handleSubmit1();
    } else if (Accountnoholder === "custom1") {
      handleSubmit2();
    }
  };

  useEffect(() => {
    handleSubmit1();
    setError("");
  }, [tcount, itemPage]);

  const handleSubmit1 = async () => {
    if (Accountnoholder === "" || duration === "") {
      setError(true);
    } else {
      setisLoadig(true);
      try {
        const response = await amsApi.post(
          `revolvingCreditCardTxn/viewRevolvngCreditCard/pagination/${itemPage}/${tcount}`,
          {
            strAccountNumber: Accountnoholder,
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
          // setTotalElement(response.data.totalElementsSize);
          setalldata(response.data.getRevolvingData);
          setCheckpoint(1);
          setChecked(false);

          setisLoadig(false);
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
    handleSubmit2();
    setError("");
  }, [tcount, itemPage]);
  const handleSubmit2 = async () => {
    if (accountno === "" || duration === "") {
      setError(true);
    } else {
      setisLoadig(true);
      try {
        const response = await amsApi.post(
          `revolvingCreditCardTxn/viewRevolvngCreditCard/pagination/${itemPage}/${tcount}`,
          {
            strAccountNumber: accountno,
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
          setalldata(response.data.getRevolvingData);
          setCheckpoint(1);
          setChecked(false);
          setisLoadig(false);
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
  const AccountnumberHandler = (e) => {
    setAccnumberOpenOpen(false);

    const selectData1 = e.target.value;
    setAccountnoHandler(selectData1);
    if (selectData1 === "custom1") {
      setAccnumberOpenOpen(true);
    } else {
      setAccountno("");
      setNameData([]);
    }
  };

  const [namedata, setNameData] = useState([]);
  const Hname = namedata.map((t) => [t.strAccountHolderName]);
  sessionStorage.setItem("Hname", Hname);
  let sname = sessionStorage.getItem("Hname");
  const accountNumber = Accountnoholder === "custom1" ? accountno : "";
  const name = async () => {
    try {
      const response = await amsApi.post(
        `account/getAccountName`,
        {
          strAccountNumber: accountNumber,
        },
        {
          headers: {
            "Content-Type": "application/json",
          },
        }
      );
      if (response.data.code === "S0000") {
        setNameData(response.data.accountName);
      } else {
        // showError(response.data.message);
      }
    } catch (error) {
      // showError(error.response.data.message);
    }
  };

  const [searchKeyword, setSearchKeyword] = useState("");
  // Function to handle changes in the search input field

  const handleSearch = (event) => {
    setSearchKeyword(event.target.value);
  };
  // Filter the acceptData array based on the searchKeyword
  const filteredData = alldata.filter((data) => {
    const {
      strAccountType,
      strAccountNumber,
      strMcc,
      strInterestRate,
      strTxnId,
      strTranType,
      strTxnAmount,
      strTranTypeDes,
      strRemaningGracePeriod,
      strUpdateTxnAmount,
      totalOutstandingInterest,
      totalCalGstAmount,
      strTotalCount,
    } = data;
    const lowerCasedSearchKeyword = searchKeyword.toLowerCase();

    return (
      (strAccountType &&
        strAccountType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strAccountNumber &&
        strAccountNumber.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strMcc && strMcc.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strInterestRate &&
        strInterestRate.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strTxnId && strTxnId.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strTranType &&
        strTranType.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strTxnAmount &&
        strTxnAmount.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strTranTypeDes &&
        strTranTypeDes.toLowerCase().includes(lowerCasedSearchKeyword)) ||
      (strRemaningGracePeriod &&
        strRemaningGracePeriod
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      (totalOutstandingInterest &&
        totalOutstandingInterest
          .toLowerCase()
          .includes(lowerCasedSearchKeyword)) ||
      (totalCalGstAmount &&
        totalCalGstAmount.toLowerCase().includes(lowerCasedSearchKeyword))
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

  const sortedData = filteredData.slice(0).sort((a, b) => {
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
      <span className="cursor-pointer   text-xs hover:bg-blue-500  hover:text-white px-0.5">
        {sortColumn === column ? (sortOrder === "asc" ? "▲" : "▼") : "⇅"}
      </span>
    );
  };
  const TotalCount = alldata.map((t) => [t.strTotalCount])[0];

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-200 ">
            <div className=" flex  items-center justify-between">
              <p className="text-base  sm:text-xm text-black :text-2xl  leading-normal ">
                View Revolving Credit Transaction
              </p>
              <span className="flex justify-between ml-4">
                <p class="px-4 flex ">
                  <span class="px-1 w-auto text-black text-sm">
                    Total Count:
                  </span>
                  <span class="border border-black text-xs rounder-lg  w-auto black py-0.5 px-2 dark:bg-gray-700 dark:border-gray-600 dark:text-white bg-white">
                    {TotalCount}
                  </span>
                </p>
              </span>
              <div>
                <button className="inline-flex sm:ml-3 mt-1 sm:mt-0 items-start justify-start px-8  focus:outline-none rounded text-xs font-normal">
                  <div className="flex justify-end  rounded-md border border-transparent px-1  text-sm font-medium text-white ">
                    <div class=" relative mx-auto text-gray-600">
                      {" "}
                      <input
                        type="search"
                        id="search"
                        value={searchKeyword}
                        onChange={handleSearch}
                        className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Search.."
                        autoComplete="off"
                        style={{
                          backgroundColor:
                            checkpoint === 1 ? "white" : "lightgray",
                        }}
                        disabled={checkpoint !== 1}
                      />
                      <button
                        title="Search Data"
                        type="submit"
                        className="absolute right-0 top-0  mt-1"
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
                  </div>
                </button>
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
              </div>
            </div>
          </div>
        </div>
        {checked && (
          <div className=" md:col-span-2 lg:col-span-1  ">
            <form className="">
              <div className="overflow-hidden shadow sm:rounded-md ">
                <div className=" px-4 sm:p-2 bg-white ">
                  <div className="grid gap-4 lg:grid-cols-5 px-5">
                    <div>
                      <label
                        htmlFor="text"
                        className="block  text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Account Number
                        <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="Accountno"
                        name="Accountno"
                        value={Accountnoholder}
                        onChange={(e) => AccountnumberHandler(e)}
                        className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select</option>
                        <option value="ALL">ALL</option>
                        <option value="custom1">Custom</option>
                      </select>
                      {error && Accountnoholder.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please Select Accountno!
                        </p>
                      ) : (
                        ""
                      )}
                    </div>
                    {accnumberOpen && (
                      <>
                        <div>
                          <label
                            htmlFor="number"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Account Number
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
                            onKeyUp={name}
                            onBlur={() => {
                              if (accountno.length === 0) {
                                setError("Please enter Account Number!");

                                setErrorMessage("");
                              }
                            }}
                            className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
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
                        <div>
                          <label
                            htmlFor="text"
                            className="block text-xs font-semibold text-gray-700 dark:text-white"
                          >
                            Account Holder Name
                            <span className="text-red-600 px-2">*</span>
                          </label>
                          <input
                            type="text"
                            id="sname"
                            defaultValue={sname}
                            className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                            autoComplete="off"
                            placeholder="Enter Account Holder Name"
                          />
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
                          Please Select Duration!
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
                            id="startdate"
                            value={startdate}
                            onChange={(e) => setStartdate(e.target.value)}
                            className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                          {/* {error && startdate.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Enter From date
                          </p>
                        ) : (
                          ""
                        )} */}
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
                            id="enddate"
                            value={enddate}
                            onChange={(e) => setEnddata(e.target.value)}
                            className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                          />
                          {/* {error && enddate.length <= 0 ? (
                          <p className="text-red-500   text-xs font-medium">
                            Please Enter To date
                          </p>
                        ) : (
                          ""
                        )} */}
                        </div>
                      </>
                    )}

                    <div className="mt-2">
                      <button
                        title="Click Search Button"
                        type="button"
                        onClick={handleSubmit}
                        data-modal-toggle="defaultModal"
                        className="inline-flex justify-center  bg-blue-500 hover:bg-blue-700 b  focus:ring-2 focus:ring-blue-800 focus:ring-offset-2 border-blue-700 py-1.5 px-5 mx-2 my-2  text-xs font-bold text-white rounded "
                      >
                        <MagnifyingGlassIcon className="h-4 w-3" />
                      </button>

                      <button
                        title="Clear Data"
                        type="button"
                        onClick={handleClick}
                        className="inline-flex justify-center  bg-rose-500 hover:bg-rose-700 b  focus:ring-2 focus:ring-rose-800 focus:ring-offset-2 border-rose-700 py-1.5 px-5 mx-2 my-2  text-xs font-bold text-white rounded "
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
      </div>
      <div className="overflow-x-auto relative shadow-md ">
        <>
          <div className="table-wrp block max-h-[33rem] max-w-[33rem] ">
            <table className="w-full text-xs text-left text-clack dark:text-blue-100 font-normal">
              <thead className="text-xm border-b sticky top-0 text-gray-400 uppercase bg-gray-100 dark:text-white">
                <th
                  scope="col"
                  className="py-2 px-2 whitespace-nowrap"
                  onClick={() => handleSort("strAccountType")}
                >
                  AccountType {renderSortArrow("strAccountType")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strAccountNumber")}
                >
                  Account Number{renderSortArrow("strAccountNumber")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("txnDate")}
                >
                  txn Date{renderSortArrow("txnDate")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("txnTime")}
                >
                  txn Time{renderSortArrow("txnTime")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strMcc")}
                >
                  Mcc{renderSortArrow("strMcc")}
                </th>

                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strTxnId")}
                >
                  Txn Id{renderSortArrow("strTxnId")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strTranType")}
                >
                  Tran Type{renderSortArrow("strTranType")}
                </th>

                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strTranTypeDes")}
                >
                  Tran Type Description{renderSortArrow("strTranTypeDes")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strTxnAmount")}
                >
                  Txn Amount{renderSortArrow("strTxnAmount")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strInterestRate")}
                >
                  Interest Rate{renderSortArrow("strInterestRate")}
                </th>

                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strRemaningGracePeriod")}
                >
                  Remaning Grace Period
                  {renderSortArrow("strRemaningGracePeriod")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("")}
                >
                  Update Txn Amount
                  {renderSortArrow("strUpdateTxnAmount")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("totalOutstandingInterest")}
                >
                  total Outstanding Interest
                  {renderSortArrow("totalOutstandingInterest")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("totalCalGstAmount")}
                >
                  total Cal Gst Amount{renderSortArrow("totalCalGstAmount")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("paymentDueDate")}
                >
                  payment Due Date{renderSortArrow("paymentDueDate")}
                </th>
              </thead>

              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData.map(
                      ({
                        strAccountType,
                        strAccountNumber,
                        strMcc,
                        strInterestRate,
                        strTxnId,
                        strTranType,
                        strTxnAmount,
                        strTranTypeDes,
                        txnTime,
                        txnDate,
                        strRemaningGracePeriod,
                        strUpdateTxnAmount,
                        totalOutstandingInterest,
                        totalCalGstAmount,
                        paymentDueDate,
                      }) => (
                        <tr
                          key={uuidv4()}
                          className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                        >
                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                strAccountType || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                strAccountNumber || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {txnDate || "-"}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {txnTime || "-"}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(strMcc || "-", searchKeyword)}
                            </div>
                          </td>

                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(strTxnId || "-", searchKeyword)}
                            </div>
                          </td>

                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                strTranType || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>

                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                strTranTypeDes || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap text-end">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                strTxnAmount || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap text-end ">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                strInterestRate || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>

                          <td className="px-2 py-2 whitespace-nowrap">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                strRemaningGracePeriod || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap text-end">
                            <div className="text-smclassName text-gray-900">
                              {strUpdateTxnAmount || "-"}
                            </div>
                          </td>

                          <td className="px-2 py-2 whitespace-nowrap text-end">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                totalOutstandingInterest || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap text-end">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                totalCalGstAmount || "-",
                                searchKeyword
                              )}
                            </div>
                          </td>
                          <td className="px-2 py-2 whitespace-nowrap ">
                            <div className="text-smclassName text-gray-900">
                              {highlightKeyword(
                                paymentDueDate || "-",
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
                    <td colSpan="12" className="px-2 py-2 text-center">
                      <span className="text-base font-medium text-gray-500">
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

import React, { useState, useEffect } from "react";
import Switch from "@mui/joy/Switch";
import { v4 as uuidv4 } from "uuid";
import Typography from "@mui/joy/Typography";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
export default function ViewBillingcycle() {
  const [checked, setChecked] = useState(true);
  const [alldata, setalldata] = useState([]);
  const [dateOpen, setDateOpen] = useState(false);
  const [accnumberOpen, setAccnumberOpenOpen] = useState(false);
  const [Accountnoholder, setAccountnoHandler] = useState("");
  const [isLoadig, setisLoadig] = useState(false);
  const [duration, setDuration] = useState("");
  const [error, setError] = useState("");
  const [accountno, setAccountno] = useState("");
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const [searchQuery, setSearchQuery] = useState("");
  const [checkpoint, setCheckpoint] = useState(0);
  const [itemPage, setItemPage] = useState(10);
  const [tcount, setTcount] = useState(1);
  const [namedata, setNameData] = useState([]);
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
          `billingCycle/viewBillingCycle/pagination/${itemPage}/${tcount}`,
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
          setalldata(response.data.billingCycleModel);
          setCheckpoint(1);
          setChecked(false);
          // handleShowSuccess(response.data.message);
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
          `billingCycle/viewBillingCycle/pagination/${itemPage}/${tcount}`,
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
          setalldata(response.data.billingCycleModel);
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
      return aValue.toString().localeCompare(bValue.toString());
    } else {
      const aValue = a[sortColumn] || "";
      const bValue = b[sortColumn] || "";
      return bValue.toString().localeCompare(aValue.toString());
    }
  });
  const renderSortArrow = (column) => {
    const arrowStyle = {
      cursor: "pointer",
      transition: "transform 0.2s",
    };

    return (
      <span className="cursor-pointer text-xs hover:bg-blue-500  hover:text-white px-0.5 ">
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
                View Billing Cycle
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
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="block w-full px-2 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0"
                        placeholder="Search.."
                        autoComplete="off"
                        style={{
                          backgroundColor:
                            checkpoint === 1 ? "white" : "lightgray",
                          // You can set other styles here as needed
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

              {/* </div> */}
            </div>
          </div>
        </div>
        {checked && (
          <div className=" md:col-span-2 lg:col-span-1">
            <form className="">
              <div className="overflow-hidden shadow sm:rounded-md">
                <div className=" px-4  sm:p-2 bg-white ">
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
                          Please Select Account Number!
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

                    <div className="mt-2  ">
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
                  Account Type{renderSortArrow("strAccountType")}
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
                  onClick={() => handleSort("strCardType")}
                >
                  Card Type{renderSortArrow("strCardType")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strCardNumber")}
                >
                  Card Number{renderSortArrow("strCardNumber")}
                </th>

                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strOutstandingAmt")}
                >
                  Outstanding Amt {renderSortArrow("strOutstandingAmt")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strAvialbleCreditLimit")}
                >
                  Available Credit Limit
                  {renderSortArrow("strAvialbleCreditLimit")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strMinimumAmountDue")}
                >
                  Minimum Amount Due{renderSortArrow("strMinimumAmountDue")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strCurrentBillingCycleDate")}
                >
                  Current Billing Cycle Date
                  {renderSortArrow("strCurrentBillingCycleDate")}
                </th>
                <th
                  scope="col"
                  className="py-2 px-2  whitespace-nowrap "
                  onClick={() => handleSort("strLastBillingCycleDate")}
                >
                  Last Billing Cycle Date
                  {renderSortArrow("strLastBillingCycleDate")}
                </th>
              </thead>

              <tbody>
                {sortedData.length > 0 ? (
                  <>
                    {sortedData
                      .filter((data) =>
                        Object.values(data).some((value) =>
                          value
                            .toString()
                            .toLowerCase()
                            .includes(searchQuery.toLowerCase())
                        )
                      )
                      .map(
                        ({
                          strAccountType,
                          strAccountNumber,
                          strCardNumber,
                          strCardType,
                          strOutstandingAmt,
                          strAvialbleCreditLimit,
                          strMinimumAmountDue,
                          strLastBillingCycleDate,
                          strCurrentBillingCycleDate,
                        }) => (
                          <tr
                            key={uuidv4()}
                            className="bg-blue-00 border-b  hover:bg-blue-200 border-blue-400"
                          >
                            <td className="px-2 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strAccountType || "-"}
                              </div>
                            </td>
                            <td className="px-2 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strAccountNumber || "-"}
                              </div>
                            </td>
                            <td className="px-2 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strCardType || "-"}
                              </div>
                            </td>
                            <td className="px-2 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strCardNumber || "-"}
                              </div>
                            </td>

                            <td className="px-2 py-2 whitespace-nowrap text-end ">
                              <div className="text-smclassName text-gray-900">
                                {strOutstandingAmt || "-"}
                              </div>
                            </td>

                            <td className="px-2 py-2 whitespace-nowrap text-end">
                              <div className="text-smclassName text-gray-900">
                                {strAvialbleCreditLimit || "-"}
                              </div>
                            </td>
                            <td className="px-2 py-2 whitespace-nowrap text-end">
                              <div className="text-smclassName text-gray-900">
                                {strMinimumAmountDue || "-"}
                              </div>
                            </td>
                            <td className="px-2 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strCurrentBillingCycleDate || "-"}
                              </div>
                            </td>
                            <td className="px-2 py-2 whitespace-nowrap">
                              <div className="text-smclassName text-gray-900">
                                {strLastBillingCycleDate || "-"}
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

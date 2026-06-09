import React, { useState, useEffect } from "react";
import AppLayout from "../../layout/AppLayout";
import amsApi from "../../api/amsApi";
import { v4 as uuidv4 } from "uuid";
import swal from "sweetalert";
import Switch from "@mui/joy/Switch";
import Typography from "@mui/joy/Typography";
import { XMarkIcon } from "@heroicons/react/24/solid";
import { MagnifyingGlassIcon } from "@heroicons/react/24/outline";
import CustomAlert from "../../layout/CustomAlert";
import AutoPagintation from "../../UI/AutoPagintation";
import AutoPagintationReport from "./AutoPagintationReport";
export default function RegisteredCus() {
  const [searchQuery, setSearchQuery] = useState("");
  const [startdate, setStartdate] = useState("");
  const [enddate, setEnddata] = useState("");
  const [error, setError] = useState("");
 
  const [status, setStatus] = useState("");

  const [showAlert, setShowAlert] = useState(false);
  const [alertTitle, setAlertTitle] = useState("");
  const [alertMessage, setAlertMessage] = useState("");
  const [alertType, setAlertType] = useState("");
  const [itemPage, setItemPage] = useState(20);
  const [tcount, setTcount] = useState(1);
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
  // List

  const [list, setList] = useState([]);
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
      enddate.length === 0 ||
      status === ""
    ) {
      setError(true);
    } else if (startdate > enddate) {
      swal("please choose lesser date from To Date");
    } else {
      try {
        const response = await amsApi.post(
          `preAccountMaster/registrCustomersList/pagination/${itemPage}/${tcount}`,
          {
            fromDate: startdate,
            strStatus: status,
            toDate: enddate,
          },
          {
            headers: {
              "Content-Type": "application/json",
            },
          }
        );
        if (response.data.code === "S0000") {
          setList(response.data.preAccountMasters);
          // handleShowSuccess(response.data.message);
          
          setChecked(false);
         
        } else {
          handleShowError(response.data.message);
          setList([]);
          setChecked(true);
        }
      } catch (error) {
        handleShowError(error.response.data.message);
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
      try {
        const response = await amsApi.post(
          `preAccountMaster/registerSearchList/pagination/${itemPage}/${tcount}`,
          {
            keyword: searchQuery,
          }
        );
        if (response.data.code === "S0000") {
          setList(response.data.preAccountMasters);
          // handleShowSuccess(response.data.message);
        } else {
          handleShowError(response.data.message);
        }
      } catch (error) {
        // handleShowError(error.response.data.message);
      }
    };
  const handleClick = () => {
    setStartdate("");
    setEnddata("");
    setStatus("");
    setDuration("");
    setError("");
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
  // const sortedData = [...list].sort((a, b) => {
    const sortedData =
      list && list.length > 0
        ? list.slice(0).sort((a, b) => {
            if (sortOrder === "asc") {
              const aValue = a[sortColumn] || "";
              const bValue = b[sortColumn] || "";
              return aValue.localeCompare(bValue);
            } else {
              const aValue = a[sortColumn] || "";
              const bValue = b[sortColumn] || "";
              return bValue.localeCompare(aValue);
            }
          })
        : [];

  return (
    <AppLayout>
      <div className="max-w-full mx-auto h-full">
        <div className="w-full shadow-md mt-2 ">
          <div className="px-4 sm:px-10 bg-blue-800">
            <div className=" flex  items-center justify-between">
              <p className="flex justify-start  text-white text-md  font-semibold leading-normal">
                Registered Customers
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
            <form className="" onSubmit={handleSubmit}>
              <div className="overflow-hidden shadow ">
                <div className=" px-4 sm:p-2 bg-white  ">
                  <div className="grid gap-6 md:grid-cols-5 px-8 ">
                    <div>
                      <label
                        htmlFor="text"
                        className="block text-xs font-semibold text-gray-700 dark:text-white"
                      >
                        Status <span className="text-red-600 px-2">*</span>
                      </label>

                      <select
                        id="accountType"
                        name="accountType"
                        value={status}
                        onChange={(e) => setStatus(e.target.value)}
                        className="block w-full px-3 py-1 text-sm font-normal text-gray-700 bg-white bg-clip-padding border border-solid border-gray-500 rounded transition ease-in-out m-0 "
                      >
                        <option value="">Select </option>
                        <option value="Y">Confirmed Customers</option>

                        <option value="N">Pending Customers</option>
                      </select>
                      {error && status.length <= 0 ? (
                        <p className="text-red-500   text-xs font-medium">
                          Please select Status
                        </p>
                      ) : (
                        ""
                      )}
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
                  <div className="grid gap-6 mb-2 mt-2 md:grid-cols-3 lg:grid-cols-3 px-8"> */}
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
        <AutoPagintationReport
          addPageperData={setItemPage}
          addCurrentPage={setTcount}
          searchData={setSearchQuery}
          pagePerData={itemPage}
          currentPage={tcount}
          tcount={list[0]?.strTotalCount}
          reCallApi={Search}
        />
        <div className="overflow-x-auto relative shadow-md">
          <>
            <div className="table-wrp block max-h-[32rem] ">
              <table className="w-full text-xs  text-left text-black dark:text-blue-100">
                <thead className="w-full text-white border-b sticky top-0 bg-blue-500 ">
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("cust_id")}
                  >
                    Cust Id {renderSortArrow("cust_id")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strDateofRegistration")}
                  >
                    Registered Date {renderSortArrow("strDateofRegistration")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strTimeofRegistration")}
                  >
                    Registered Time {renderSortArrow("strTimeofRegistration")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strFirstName")}
                  >
                    First Name {renderSortArrow("strFirstName")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strMiddleName")}
                  >
                    Middle Name {renderSortArrow("strMiddleName")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strLastName")}
                  >
                    Last Name {renderSortArrow("strLastName")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strAccountRegistered")}
                  >
                    A/c Registration {renderSortArrow("strAccountRegistered")}
                  </th>
                  <th
                    scope="col"
                    className="py-1 px-4 whitespace-nowrap"
                    onClick={() => handleSort("strAgeing")}
                  >
                    Ageing {renderSortArrow("strAgeing")}
                  </th>
                </thead>

                <tbody>
                  {sortedData.length > 0 ? (
                    <>
                      {sortedData.map((data) => (
                        <tr
                          key={uuidv4()}
                          className=" border-b dark:border-neutral-500"
                        >
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.cust_id || "-"}
                            </div>
                          </td>

                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strDateofRegistration || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strTimeofRegistration || "-"}
                            </div>
                          </td>

                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strFirstName || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strMiddleName || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strLastName || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strAccountRegistered || "-"}
                            </div>
                          </td>
                          <td className="px-4 py-1 whitespace-nowrap">
                            <div className="text-xs font-medium text-gray-900">
                              {data.strAgeing || "-"}
                            </div>
                          </td>
                        </tr>
                      ))}
                    </>
                  ) : (
                    <tr>
                      <td colSpan="10" className="px-4 py-2 text-center">
                        <span className="text-xs font-normal text-gray-500">
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
